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

import io.fireflysoftware.common.platform.notification.services.sdk.api.SmsNotificationsApi;
import io.fireflysoftware.common.platform.notification.services.sdk.model.SMSResponseDTO;
import io.fireflysoftware.domain.common.notifications.core.send.commands.AuditSmsNotificationCommand;
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
class AuditSmsHandlerTest {

    @Mock private SmsNotificationsApi smsNotificationsApi;

    private AuditSmsHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AuditSmsHandler(smsNotificationsApi);
    }

    @Test
    void doHandle_auditSucceeds_returnsNotificationId() {
        UUID expectedId = UUID.randomUUID();

        AuditSmsNotificationCommand cmd = AuditSmsNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientPhone("+34612345678")
                .message("Your verification code is 1234")
                .deliveryResult(DeliveryResult.success("provider-msg-id"))
                .build();

        SMSResponseDTO auditResponse = new SMSResponseDTO();
        auditResponse.setMessageId(expectedId.toString());
        when(smsNotificationsApi.sendSMS(any(), any()))
                .thenReturn(Mono.just(auditResponse));

        StepVerifier.create(handler.doHandle(cmd))
                .expectNext(expectedId)
                .verifyComplete();
    }

    @Test
    void doHandle_auditReturnsNoId_errors() {
        AuditSmsNotificationCommand cmd = AuditSmsNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientPhone("+34612345678")
                .message("Your code is 1234")
                .deliveryResult(DeliveryResult.failed("Twilio unavailable"))
                .build();

        SMSResponseDTO auditResponse = new SMSResponseDTO();
        auditResponse.setMessageId(null);
        when(smsNotificationsApi.sendSMS(any(), any()))
                .thenReturn(Mono.just(auditResponse));

        StepVerifier.create(handler.doHandle(cmd))
                .expectError(IllegalStateException.class)
                .verify();
    }
}
