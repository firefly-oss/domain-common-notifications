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
import io.fireflysoftware.domain.common.notifications.core.send.queries.GetNotificationDetailQuery;
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
