package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.DeleteNotificationCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

/**
 * Deletes a specific notification.
 * <p>
 * TODO: Replace stub with real persistence when notification read-model is available.
 * </p>
 */
@CommandHandlerComponent
@Slf4j
public class DeleteNotificationHandler extends CommandHandler<DeleteNotificationCommand, Void> {

    @Override
    protected Mono<Void> doHandle(DeleteNotificationCommand cmd) {
        log.info("Deleting notification: notificationId={}, partyId={}", cmd.getNotificationId(), cmd.getPartyId());
        // TODO: Replace stub with core SDK call when notification read-model is available
        return Mono.empty();
    }
}
