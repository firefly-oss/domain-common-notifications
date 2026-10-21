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

import io.fireflysoftware.domain.common.notifications.core.send.model.NotificationDTO;
import io.fireflysoftware.domain.common.notifications.core.send.queries.GetNotificationsByPartyQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * Retrieves paginated notifications for a party.
 * <p>
 * TODO: Replace stub with real persistence when notification read-model is available.
 * </p>
 */
@QueryHandlerComponent
@Slf4j
public class GetNotificationsByPartyHandler extends QueryHandler<GetNotificationsByPartyQuery, List<NotificationDTO>> {

    @Override
    protected Mono<List<NotificationDTO>> doHandle(GetNotificationsByPartyQuery query) {
        log.debug("Fetching notifications: partyId={}, page={}, size={}", query.getPartyId(), query.getPage(), query.getSize());
        // TODO: Replace stub with core SDK call when notification read-model is available
        return Mono.just(Collections.emptyList());
    }
}
