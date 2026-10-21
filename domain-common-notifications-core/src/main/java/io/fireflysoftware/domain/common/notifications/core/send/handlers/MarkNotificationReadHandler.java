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

import io.fireflysoftware.domain.common.notifications.core.send.commands.MarkNotificationReadCommand;
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
