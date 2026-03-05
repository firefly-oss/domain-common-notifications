package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.model.NotificationPreferencesDTO;
import com.firefly.domain.common.notifications.core.send.queries.GetPreferencesQuery;
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
