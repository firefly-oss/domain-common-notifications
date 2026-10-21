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
import io.fireflysoftware.domain.common.notifications.core.send.store.SentNotificationStore;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.annotations.QueryHandlerComponent;
import org.fireflyframework.cqrs.query.QueryHandler;
import reactor.core.publisher.Mono;

/**
 * Retrieves one message previously sent through the notification core.
 */
@QueryHandlerComponent
@Slf4j
public class GetNotificationDetailHandler extends QueryHandler<GetNotificationDetailQuery, NotificationDTO> {

    private final SentNotificationStore sentNotifications;

    public GetNotificationDetailHandler(SentNotificationStore sentNotifications) {
        this.sentNotifications = sentNotifications;
    }

    @Override
    protected Mono<NotificationDTO> doHandle(GetNotificationDetailQuery query) {
        log.debug("Fetching notification detail: notificationId={}", query.getNotificationId());
        return Mono.justOrEmpty(sentNotifications.find(query.getNotificationId()));
    }
}
