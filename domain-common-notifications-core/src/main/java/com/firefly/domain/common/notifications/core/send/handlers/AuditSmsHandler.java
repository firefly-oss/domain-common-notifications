package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.common.platform.notification.services.sdk.api.SmsNotificationsApi;
import com.firefly.domain.common.notifications.core.send.commands.AuditSmsNotificationCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Records an SMS notification in the core audit database via
 * {@link SmsNotificationsApi}. Receives the {@link com.firefly.domain.common.notifications.core.send.model.DeliveryResult}
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
        var sdkRequest = new com.firefly.common.platform.notification.services.sdk.model.SMSRequestDTO()
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
