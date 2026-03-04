package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.common.platform.notification.services.sdk.api.SmsNotificationsApi;
import com.firefly.common.platform.notification.services.sdk.model.SMSResponseDTO;
import com.firefly.domain.common.notifications.core.send.commands.AuditSmsNotificationCommand;
import com.firefly.domain.common.notifications.core.send.model.DeliveryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        when(smsNotificationsApi.sendSMS(any(), anyString()))
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
        when(smsNotificationsApi.sendSMS(any(), anyString()))
                .thenReturn(Mono.just(auditResponse));

        StepVerifier.create(handler.doHandle(cmd))
                .expectError(IllegalStateException.class)
                .verify();
    }
}
