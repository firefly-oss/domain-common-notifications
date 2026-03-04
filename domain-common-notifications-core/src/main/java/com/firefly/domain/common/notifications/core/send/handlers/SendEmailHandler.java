package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.SendEmailNotificationCommand;
import com.firefly.domain.common.notifications.core.send.model.DeliveryResult;
import com.firefly.domain.common.notifications.infra.NotificationDeliveryProperties;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import org.fireflyframework.notifications.interfaces.dtos.email.v1.EmailRequestDTO;
import org.fireflyframework.notifications.interfaces.providers.email.v1.EmailProvider;
import reactor.core.publisher.Mono;

/**
 * Handles email delivery through the framework {@link EmailProvider} port.
 * Returns a {@link DeliveryResult} indicating success or failure —
 * audit recording is handled by a separate saga step.
 */
@CommandHandlerComponent
@Slf4j
public class SendEmailHandler extends CommandHandler<SendEmailNotificationCommand, DeliveryResult> {

    private final EmailProvider emailProvider;
    private final NotificationDeliveryProperties deliveryProperties;

    public SendEmailHandler(EmailProvider emailProvider,
                            NotificationDeliveryProperties deliveryProperties) {
        this.emailProvider = emailProvider;
        this.deliveryProperties = deliveryProperties;
    }

    @Override
    protected Mono<DeliveryResult> doHandle(SendEmailNotificationCommand cmd) {
        EmailRequestDTO deliveryRequest = EmailRequestDTO.builder()
                .from(deliveryProperties.getDefaultSenderEmail())
                .to(cmd.getRecipientEmail())
                .subject(cmd.getSubject())
                .text(cmd.getBody())
                .build();

        return emailProvider.sendEmail(deliveryRequest)
                .map(response -> DeliveryResult.success(response.getMessageId()))
                .onErrorResume(ex -> {
                    log.warn("Email delivery failed for partyId={}: {}",
                            cmd.getPartyId(), ex.getMessage());
                    return Mono.just(DeliveryResult.failed(ex.getMessage()));
                });
    }
}
