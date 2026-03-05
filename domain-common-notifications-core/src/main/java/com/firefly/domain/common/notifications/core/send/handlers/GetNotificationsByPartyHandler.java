package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.model.NotificationDTO;
import com.firefly.domain.common.notifications.core.send.queries.GetNotificationsByPartyQuery;
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
