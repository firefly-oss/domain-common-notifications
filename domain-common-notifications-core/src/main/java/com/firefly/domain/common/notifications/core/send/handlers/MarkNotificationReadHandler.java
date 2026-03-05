package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.MarkNotificationReadCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

/**
 * Marks a specific notification as read.
 * <p>
 * TODO: Replace stub with real persistence when notification read-model is available.
 * </p>
 */
@CommandHandlerComponent
@Slf4j
public class MarkNotificationReadHandler extends CommandHandler<MarkNotificationReadCommand, Void> {

    @Override
    protected Mono<Void> doHandle(MarkNotificationReadCommand cmd) {
        log.info("Marking notification as read: notificationId={}, partyId={}", cmd.getNotificationId(), cmd.getPartyId());
        // TODO: Replace stub with core SDK call when notification read-model is available
        return Mono.empty();
    }
}
