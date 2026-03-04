package com.firefly.domain.common.notifications.core.send.services.impl;

import com.firefly.domain.common.notifications.core.send.commands.SendBulkNotificationCommand;
import com.firefly.domain.common.notifications.core.send.commands.SendNotificationCommand;
import com.firefly.domain.common.notifications.core.send.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.fireflyframework.orchestration.saga.engine.SagaEngine;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.fireflyframework.orchestration.saga.engine.StepInputs;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.firefly.domain.common.notifications.core.utils.constants.NotificationConstants.*;

/**
 * Default implementation of {@link NotificationService}.
 * <p>
 * Uses the {@link SagaEngine} to execute notification workflows,
 * routing each notification through the {@code SendNotificationSaga}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SagaEngine engine;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SagaResult> sendNotification(SendNotificationCommand command) {
        StepInputs inputs = StepInputs.builder()
                .forStepId(STEP_ROUTE_NOTIFICATION, command)
                .forStepId(STEP_DELIVER_EMAIL, command)
                .forStepId(STEP_DELIVER_SMS, command)
                .forStepId(STEP_AUDIT_EMAIL, command)
                .forStepId(STEP_AUDIT_SMS, command)
                .build();

        return engine.execute(SAGA_SEND_NOTIFICATION, inputs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SagaResult> sendBulkNotifications(SendBulkNotificationCommand command) {
        return Flux.fromIterable(command.getNotifications())
                .flatMap(this::sendNotification)
                .last()
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Bulk notification command contained no notifications")));
    }
}
