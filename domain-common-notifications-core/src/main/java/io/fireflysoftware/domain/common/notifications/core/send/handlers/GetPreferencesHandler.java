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

import io.fireflysoftware.domain.common.notifications.core.send.model.NotificationPreferencesDTO;
import io.fireflysoftware.domain.common.notifications.core.send.queries.GetPreferencesQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Retrieves notification preferences for a party.
 * <p>
 * TODO: Replace stub with real persistence when preference store is available.
 * </p>
 */
@QueryHandlerComponent
@Slf4j
public class GetPreferencesHandler extends QueryHandler<GetPreferencesQuery, NotificationPreferencesDTO> {

    @Override
    protected Mono<NotificationPreferencesDTO> doHandle(GetPreferencesQuery query) {
        log.debug("Fetching notification preferences: partyId={}", query.getPartyId());
        // TODO: Replace stub with real preference store
        return Mono.just(NotificationPreferencesDTO.builder()
                .partyId(query.getPartyId())
                .preferences(Map.of("EMAIL", true, "SMS", true, "PUSH", true))
                .build());
    }
}
