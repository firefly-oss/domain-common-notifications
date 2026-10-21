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

import io.fireflysoftware.common.platform.notification.services.sdk.api.SmsNotificationsApi;
import io.fireflysoftware.domain.common.notifications.core.send.commands.AuditSmsNotificationCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Records an SMS notification in the core audit database via
 * {@link SmsNotificationsApi}. Receives the {@link io.fireflysoftware.domain.common.notifications.core.send.model.DeliveryResult}
 * from the delivery step so the audit record reflects actual delivery outcome.
 */
@CommandHandlerComponent
@Slf4j
public class AuditSmsHandler extends CommandHandler<AuditSmsNotificationCommand, UUID> {

    private final SmsNotificationsApi smsNotificationsApi;

    public AuditSmsHandler(SmsNotificationsApi smsNotificationsApi) {
        this.smsNotificationsApi = smsNotificationsApi;
    }

    @Override
    protected Mono<UUID> doHandle(AuditSmsNotificationCommand cmd) {
        var sdkRequest = new io.fireflysoftware.common.platform.notification.services.sdk.model.SMSRequestDTO()
                .phoneNumber(cmd.getRecipientPhone())
                .message(cmd.getMessage());

        return smsNotificationsApi.sendSMS(sdkRequest, UUID.randomUUID().toString())
                .flatMap(response -> {
                    if (response.getMessageId() != null) {
                        return Mono.just(UUID.fromString(response.getMessageId()));
                    }
                    return Mono.error(new IllegalStateException(
                            "SMS audit returned no notification identifier for partyId=" + cmd.getPartyId()));
                });
    }
}
