package com.firefly.domain.common.notifications.core.send.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Notification detail representation used in query responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private UUID notificationId;
    private UUID partyId;
    private String channel;
    private String subject;
    private String body;
    private boolean read;
    private Instant createdAt;
    private Instant readAt;
}
