package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.model.NotificationDTO;
import com.firefly.domain.common.notifications.core.send.queries.GetNotificationDetailQuery;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

/**
 * Retrieves the detail of a specific notification.
 * <p>
 * TODO: Replace stub with real persistence when notification read-model is available.
 * </p>
 */
@QueryHandlerComponent
@Slf4j
public class GetNotificationDetailHandler extends QueryHandler<GetNotificationDetailQuery, NotificationDTO> {

    @Override
    protected Mono<NotificationDTO> doHandle(GetNotificationDetailQuery query) {
        log.debug("Fetching notification detail: notificationId={}", query.getNotificationId());
        // TODO: Replace stub with core SDK call when notification read-model is available
        return Mono.empty();
    }
}
