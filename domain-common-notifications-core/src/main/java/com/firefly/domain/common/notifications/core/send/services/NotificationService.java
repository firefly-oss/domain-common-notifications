package com.firefly.domain.common.notifications.core.send.services;

import com.firefly.domain.common.notifications.core.send.commands.SendBulkNotificationCommand;
import com.firefly.domain.common.notifications.core.send.commands.SendNotificationCommand;
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
