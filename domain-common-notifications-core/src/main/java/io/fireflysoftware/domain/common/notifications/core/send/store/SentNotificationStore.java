/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.domain.common.notifications.core.send.store;

import io.fireflysoftware.domain.common.notifications.core.send.model.NotificationDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Record of outbound messages this service has already sent through the notification core.
 * Inbox reads, read-state changes, and deletes operate on this record.
 */
@Component
public class SentNotificationStore {

    private final ConcurrentHashMap<UUID, NotificationDTO> byId = new ConcurrentHashMap<>();

    /**
     * Stores a message the core accepted for delivery.
     *
     * @param notificationId identifier returned by the core
     * @param partyId        party the message was sent for
     * @param channel        delivery channel
     * @param subject        message subject, when the channel has one
     * @param body           message body
     */
    public void recordSent(UUID notificationId, UUID partyId, String channel, String subject, String body) {
        NotificationDTO stored = NotificationDTO.builder()
                .notificationId(notificationId)
                .partyId(partyId)
                .channel(channel)
                .subject(subject)
                .body(body)
                .read(false)
                .createdAt(Instant.now())
                .build();
        byId.put(notificationId, stored);
    }

    /**
     * Returns one page of a party's messages, newest first.
     *
     * @param partyId party whose messages are listed
     * @param page    zero-based page index
     * @param size    page size
     * @return the page, or an empty list when the party has no messages on that page
     */
    public List<NotificationDTO> listByParty(UUID partyId, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        return byId.values().stream()
                .filter(notification -> partyId.equals(notification.getPartyId()))
                .sorted(Comparator.comparing(NotificationDTO::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .skip((long) safePage * safeSize)
                .limit(safeSize)
                .map(this::copy)
                .toList();
    }

    /**
     * Returns one stored message.
     *
     * @param notificationId message identifier
     * @return the message, or empty when it was never sent or has been deleted
     */
    public Optional<NotificationDTO> find(UUID notificationId) {
        NotificationDTO found = byId.get(notificationId);
        return found == null ? Optional.empty() : Optional.of(copy(found));
    }

    /**
     * Marks one of the party's messages as read.
     *
     * @param notificationId message identifier
     * @param partyId        party that owns the message
     * @return {@code true} when the message belongs to the party
     */
    public boolean markRead(UUID notificationId, UUID partyId) {
        NotificationDTO[] updated = new NotificationDTO[1];
        byId.compute(notificationId, (id, existing) -> {
            if (existing == null || !partyId.equals(existing.getPartyId())) {
                return existing;
            }
            if (existing.isRead()) {
                updated[0] = existing;
                return existing;
            }
            NotificationDTO next = copy(existing);
            next.setRead(true);
            next.setReadAt(Instant.now());
            updated[0] = next;
            return next;
        });
        return updated[0] != null;
    }

    /**
     * Marks every stored message for the party as read.
     *
     * @param partyId party whose messages are marked read
     */
    public void markAllRead(UUID partyId) {
        Instant readAt = Instant.now();
        byId.replaceAll((id, existing) -> {
            if (!partyId.equals(existing.getPartyId()) || existing.isRead()) {
                return existing;
            }
            NotificationDTO next = copy(existing);
            next.setRead(true);
            next.setReadAt(readAt);
            return next;
        });
    }

    /**
     * Removes one of the party's messages.
     *
     * @param notificationId message identifier
     * @param partyId        party that owns the message
     * @return {@code true} when a message belonging to the party was removed
     */
    public boolean delete(UUID notificationId, UUID partyId) {
        boolean[] removed = {false};
        byId.compute(notificationId, (id, existing) -> {
            if (existing != null && partyId.equals(existing.getPartyId())) {
                removed[0] = true;
                return null;
            }
            return existing;
        });
        return removed[0];
    }

    private NotificationDTO copy(NotificationDTO source) {
        return NotificationDTO.builder()
                .notificationId(source.getNotificationId())
                .partyId(source.getPartyId())
                .channel(source.getChannel())
                .subject(source.getSubject())
                .body(source.getBody())
                .read(source.isRead())
                .createdAt(source.getCreatedAt())
                .readAt(source.getReadAt())
                .build();
    }
}
