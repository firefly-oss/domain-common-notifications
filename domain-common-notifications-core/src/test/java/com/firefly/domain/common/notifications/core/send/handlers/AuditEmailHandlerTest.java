package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.common.platform.notification.services.sdk.api.EmailNotificationsApi;
import com.firefly.common.platform.notification.services.sdk.model.EmailResponseDTO;
import com.firefly.domain.common.notifications.core.send.commands.AuditEmailNotificationCommand;
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
        when(emailNotificationsApi.sendEmail(any(), anyString()))
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
        when(emailNotificationsApi.sendEmail(any(), anyString()))
                .thenReturn(Mono.just(auditResponse));

        StepVerifier.create(handler.doHandle(cmd))
                .expectError(IllegalStateException.class)
                .verify();
    }
}
