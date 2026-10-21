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
import io.fireflysoftware.domain.common.notifications.core.send.store.SentNotificationStore;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Retrieves paginated notifications previously sent for a party.
 */
@QueryHandlerComponent
@Slf4j
public class GetNotificationsByPartyHandler extends QueryHandler<GetNotificationsByPartyQuery, List<NotificationDTO>> {

    private final SentNotificationStore sentNotifications;

    public GetNotificationsByPartyHandler(SentNotificationStore sentNotifications) {
        this.sentNotifications = sentNotifications;
    }

    @Override
    protected Mono<List<NotificationDTO>> doHandle(GetNotificationsByPartyQuery query) {
        log.debug("Fetching notifications: partyId={}, page={}, size={}", query.getPartyId(), query.getPage(), query.getSize());
        return Mono.just(sentNotifications.listByParty(query.getPartyId(), query.getPage(), query.getSize()));
    }
}
