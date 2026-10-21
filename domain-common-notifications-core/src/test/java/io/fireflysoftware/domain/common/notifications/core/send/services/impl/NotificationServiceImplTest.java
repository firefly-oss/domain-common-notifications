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


package io.fireflysoftware.domain.common.notifications.core.send.services.impl;

import io.fireflysoftware.domain.common.notifications.core.send.commands.SendBulkNotificationCommand;
import io.fireflysoftware.domain.common.notifications.core.send.commands.SendNotificationCommand;
import org.fireflyframework.orchestration.saga.engine.SagaEngine;
import org.fireflyframework.orchestration.saga.engine.SagaResult;
import org.fireflyframework.orchestration.saga.engine.StepInputs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private SagaEngine sagaEngine;

    private NotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new NotificationServiceImpl(sagaEngine);
    }

    @Test
    void sendNotification_executesSaga() {
        SendNotificationCommand cmd = SendNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .channel("EMAIL")
                .recipientEmail("test@example.com")
                .subject("Test")
                .body("Test body")
                .build();

        SagaResult mockResult = mock(SagaResult.class);
        when(sagaEngine.execute(eq("SendNotificationSaga"), any(StepInputs.class)))
                .thenReturn(Mono.just(mockResult));

        StepVerifier.create(service.sendNotification(cmd))
                .expectNext(mockResult)
                .verifyComplete();
    }

    @Test
    void sendBulkNotifications_executesMultipleSagas() {
        SendNotificationCommand cmd1 = SendNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .channel("EMAIL")
                .recipientEmail("user1@example.com")
                .subject("Test 1")
                .body("Body 1")
                .build();

        SendNotificationCommand cmd2 = SendNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .channel("SMS")
                .recipientPhone("+34600000001")
                .body("Body 2")
                .build();

        SendBulkNotificationCommand bulkCmd = SendBulkNotificationCommand.builder()
                .notifications(List.of(cmd1, cmd2))
                .build();

        SagaResult mockResult = mock(SagaResult.class);
        when(sagaEngine.execute(eq("SendNotificationSaga"), any(StepInputs.class)))
                .thenReturn(Mono.just(mockResult));

        StepVerifier.create(service.sendBulkNotifications(bulkCmd))
                .expectNext(mockResult)
                .verifyComplete();
    }
}
