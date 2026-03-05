package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.MarkAllNotificationsReadCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

/**
 * Marks all notifications for a party as read.
 * <p>
 * TODO: Replace stub with real persistence when notification read-model is available.
 * </p>
 */
@CommandHandlerComponent
@Slf4j
public class MarkAllNotificationsReadHandler extends CommandHandler<MarkAllNotificationsReadCommand, Void> {

    @Override
    protected Mono<Void> doHandle(MarkAllNotificationsReadCommand cmd) {
        log.info("Marking all notifications as read: partyId={}", cmd.getPartyId());
        // TODO: Replace stub with core SDK call when notification read-model is available
        return Mono.empty();
    }
}
