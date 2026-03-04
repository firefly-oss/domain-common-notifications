package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.SendSmsNotificationCommand;
import com.firefly.domain.common.notifications.core.send.model.DeliveryResult;
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
