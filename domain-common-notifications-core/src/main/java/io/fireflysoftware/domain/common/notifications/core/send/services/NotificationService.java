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


package io.fireflysoftware.domain.common.notifications.core.send.services;

import io.fireflysoftware.domain.common.notifications.core.send.commands.SendBulkNotificationCommand;
import io.fireflysoftware.domain.common.notifications.core.send.commands.SendNotificationCommand;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import reactor.core.publisher.Mono;

/**
 * Service interface for notification orchestration.
 * <p>
 * Defines the contract for sending single and bulk notifications
 * through the saga-based notification workflow.
 * </p>
 */
public interface NotificationService {

    /**
     * Sends a single notification through the appropriate channel.
     *
     * @param command the notification command containing channel, recipient and content details
     * @return the saga execution result
     */
    Mono<SagaResult> sendNotification(SendNotificationCommand command);

    /**
     * Sends multiple notifications in bulk.
     *
     * @param command the bulk notification command containing a list of individual notifications
     * @return the saga execution result for the last notification
     */
    Mono<SagaResult> sendBulkNotifications(SendBulkNotificationCommand command);
}
