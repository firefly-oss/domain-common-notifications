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

import io.fireflysoftware.domain.common.notifications.core.send.commands.SendSmsNotificationCommand;
import io.fireflysoftware.domain.common.notifications.core.send.model.DeliveryResult;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import org.fireflyframework.notifications.interfaces.dtos.sms.v1.SMSRequestDTO;
import org.fireflyframework.notifications.interfaces.providers.sms.v1.SMSProvider;
import reactor.core.publisher.Mono;

/**
 * Handles SMS delivery through the framework {@link SMSProvider} port.
 * Returns a {@link DeliveryResult} indicating success or failure —
 * audit recording is handled by a separate saga step.
 */
@CommandHandlerComponent
@Slf4j
public class SendSmsHandler extends CommandHandler<SendSmsNotificationCommand, DeliveryResult> {

    private final SMSProvider smsProvider;

    public SendSmsHandler(SMSProvider smsProvider) {
        this.smsProvider = smsProvider;
    }

    @Override
    protected Mono<DeliveryResult> doHandle(SendSmsNotificationCommand cmd) {
        SMSRequestDTO deliveryRequest = SMSRequestDTO.builder()
                .phoneNumber(cmd.getRecipientPhone())
                .message(cmd.getMessage())
                .build();

        return smsProvider.sendSMS(deliveryRequest)
                .map(response -> DeliveryResult.success(response.getMessageId()))
                .onErrorResume(ex -> {
                    log.warn("SMS delivery failed for partyId={}: {}",
                            cmd.getPartyId(), ex.getMessage());
                    return Mono.just(DeliveryResult.failed(ex.getMessage()));
                });
    }
}
