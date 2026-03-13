package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.common.platform.notification.services.sdk.api.EmailNotificationsApi;
import com.firefly.domain.common.notifications.core.send.commands.AuditEmailNotificationCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Records an email notification in the core audit database via
 * {@link EmailNotificationsApi}. Receives the {@link com.firefly.domain.common.notifications.core.send.model.DeliveryResult}
 * from the delivery step so the audit record reflects actual delivery outcome.
 */
@CommandHandlerComponent
@Slf4j
public class AuditEmailHandler extends CommandHandler<AuditEmailNotificationCommand, UUID> {

    private final EmailNotificationsApi emailNotificationsApi;

    public AuditEmailHandler(EmailNotificationsApi emailNotificationsApi) {
        this.emailNotificationsApi = emailNotificationsApi;
    }

    @Override
    protected Mono<UUID> doHandle(AuditEmailNotificationCommand cmd) {
        var sdkRequest = new com.firefly.common.platform.notification.services.sdk.model.EmailRequestDTO()
                .to(cmd.getRecipientEmail())
                .subject(cmd.getSubject())
                .text(cmd.getBody());

        return emailNotificationsApi.sendEmail(sdkRequest)
                .flatMap(response -> {
                    if (response.getMessageId() != null) {
                        return Mono.just(UUID.fromString(response.getMessageId()));
                    }
                    return Mono.error(new IllegalStateException(
                            "Email audit returned no notification identifier for partyId=" + cmd.getPartyId()));
                });
    }
}
