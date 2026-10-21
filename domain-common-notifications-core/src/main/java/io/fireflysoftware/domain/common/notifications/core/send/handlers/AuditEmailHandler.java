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


package io.fireflysoftware.domain.common.notifications.core.send.handlers;

import io.fireflysoftware.common.platform.notification.services.sdk.api.EmailNotificationsApi;
import io.fireflysoftware.domain.common.notifications.core.send.commands.AuditEmailNotificationCommand;
import io.fireflysoftware.domain.common.notifications.core.send.store.SentNotificationStore;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.fireflysoftware.domain.common.notifications.core.utils.constants.NotificationConstants.CHANNEL_EMAIL;

/**
 * Sends the email through the notification core, then keeps the accepted message
 * so inbox reads can return it. The core itself only sends.
 */
@CommandHandlerComponent
@Slf4j
public class AuditEmailHandler extends CommandHandler<AuditEmailNotificationCommand, UUID> {

    private final EmailNotificationsApi emailNotificationsApi;
    private final SentNotificationStore sentNotifications;

    public AuditEmailHandler(EmailNotificationsApi emailNotificationsApi, SentNotificationStore sentNotifications) {
        this.emailNotificationsApi = emailNotificationsApi;
        this.sentNotifications = sentNotifications;
    }

    @Override
    protected Mono<UUID> doHandle(AuditEmailNotificationCommand cmd) {
        var sdkRequest = new io.fireflysoftware.common.platform.notification.services.sdk.model.EmailRequestDTO()
                .to(cmd.getRecipientEmail())
                .subject(cmd.getSubject())
                .text(cmd.getBody());

        return emailNotificationsApi.sendEmail(sdkRequest, UUID.randomUUID().toString())
                .flatMap(response -> {
                    if (response.getMessageId() == null) {
                        return Mono.error(new IllegalStateException(
                                "Email audit returned no notification identifier for partyId=" + cmd.getPartyId()));
                    }
                    UUID notificationId = UUID.fromString(response.getMessageId());
                    if (accepted(response.getStatus())) {
                        sentNotifications.recordSent(
                                notificationId, cmd.getPartyId(), CHANNEL_EMAIL, cmd.getSubject(), cmd.getBody());
                    }
                    return Mono.just(notificationId);
                });
    }

    private static boolean accepted(Object status) {
        return status == null || !"FAILED".equalsIgnoreCase(String.valueOf(status));
    }
}
