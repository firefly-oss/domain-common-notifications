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

import io.fireflysoftware.domain.common.notifications.core.send.commands.SendSmsNotificationCommand;
import io.fireflysoftware.domain.common.notifications.core.send.model.DeliveryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.fireflyframework.notifications.interfaces.providers.sms.v1.SMSProvider;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendSmsHandlerTest {

    @Mock private SMSProvider smsProvider;

    private SendSmsHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SendSmsHandler(smsProvider);
    }

    @Test
    void doHandle_deliverySucceeds_returnsDeliveryResultSuccess() {
        SendSmsNotificationCommand cmd = SendSmsNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientPhone("+34612345678")
                .message("Your verification code is 1234")
                .build();

        var deliveryResponse = org.fireflyframework.notifications.interfaces.dtos.sms.v1.SMSResponseDTO
                .success("provider-msg-id");
        when(smsProvider.sendSMS(any())).thenReturn(Mono.just(deliveryResponse));

        StepVerifier.create(handler.doHandle(cmd))
                .assertNext(result -> {
                    assertThat(result.isDelivered()).isTrue();
                    assertThat(result.getMessageId()).isEqualTo("provider-msg-id");
                    assertThat(result.getErrorMessage()).isNull();
                })
                .verifyComplete();
    }

    @Test
    void doHandle_deliveryFails_returnsDeliveryResultFailed() {
        SendSmsNotificationCommand cmd = SendSmsNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientPhone("+34612345678")
                .message("Your code is 1234")
                .build();

        when(smsProvider.sendSMS(any()))
                .thenReturn(Mono.error(new RuntimeException("Twilio unavailable")));

        StepVerifier.create(handler.doHandle(cmd))
                .assertNext(result -> {
                    assertThat(result.isDelivered()).isFalse();
                    assertThat(result.getErrorMessage()).isEqualTo("Twilio unavailable");
                    assertThat(result.getMessageId()).isNull();
                })
                .verifyComplete();
    }
}
