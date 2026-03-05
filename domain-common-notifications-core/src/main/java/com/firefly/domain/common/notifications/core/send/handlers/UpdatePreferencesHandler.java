package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.UpdatePreferencesCommand;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.CommandHandlerComponent;
import org.fireflyframework.cqrs.command.CommandHandler;
import reactor.core.publisher.Mono;

/**
 * Updates notification preferences for a party.
 * <p>
 * TODO: Replace stub with real persistence when preference store is available.
 * </p>
 */
@CommandHandlerComponent
@Slf4j
public class UpdatePreferencesHandler extends CommandHandler<UpdatePreferencesCommand, Void> {

    @Override
    protected Mono<Void> doHandle(UpdatePreferencesCommand cmd) {
        log.info("Updating notification preferences: partyId={}", cmd.getPartyId());
        // TODO: Replace stub with real persistence when preference store is available
        return Mono.empty();
    }
}
