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

import io.fireflysoftware.common.platform.notification.services.sdk.api.EmailNotificationsApi;
import io.fireflysoftware.common.platform.notification.services.sdk.model.EmailResponseDTO;
import io.fireflysoftware.domain.common.notifications.core.send.commands.AuditEmailNotificationCommand;
import io.fireflysoftware.domain.common.notifications.core.send.model.DeliveryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditEmailHandlerTest {

    @Mock private EmailNotificationsApi emailNotificationsApi;

    private AuditEmailHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AuditEmailHandler(emailNotificationsApi);
    }

    @Test
    void doHandle_auditSucceeds_returnsNotificationId() {
        UUID expectedId = UUID.randomUUID();

        AuditEmailNotificationCommand cmd = AuditEmailNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientEmail("test@example.com")
                .subject("Welcome")
                .body("Hello, welcome to Firefly!")
                .deliveryResult(DeliveryResult.success("provider-msg-id"))
                .build();

        EmailResponseDTO auditResponse = new EmailResponseDTO();
        auditResponse.setMessageId(expectedId.toString());
        when(emailNotificationsApi.sendEmail(any(), any()))
                .thenReturn(Mono.just(auditResponse));

        StepVerifier.create(handler.doHandle(cmd))
                .expectNext(expectedId)
                .verifyComplete();
    }

    @Test
    void doHandle_auditReturnsNoId_errors() {
        AuditEmailNotificationCommand cmd = AuditEmailNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientEmail("test@example.com")
                .subject("Welcome")
                .body("Hello!")
                .deliveryResult(DeliveryResult.success("provider-msg-id"))
                .build();

        EmailResponseDTO auditResponse = new EmailResponseDTO();
        auditResponse.setMessageId(null);
        when(emailNotificationsApi.sendEmail(any(), any()))
                .thenReturn(Mono.just(auditResponse));

        StepVerifier.create(handler.doHandle(cmd))
                .expectError(IllegalStateException.class)
                .verify();
    }
}
