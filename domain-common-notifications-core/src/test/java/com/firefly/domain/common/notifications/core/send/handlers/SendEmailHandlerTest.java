package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.SendEmailNotificationCommand;
import com.firefly.domain.common.notifications.core.send.model.DeliveryResult;
import com.firefly.domain.common.notifications.infra.NotificationDeliveryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.fireflyframework.notifications.interfaces.providers.email.v1.EmailProvider;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendEmailHandlerTest {

    @Mock private EmailProvider emailProvider;
    @Mock private NotificationDeliveryProperties deliveryProperties;

    private SendEmailHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SendEmailHandler(emailProvider, deliveryProperties);
    }

    @Test
    void doHandle_deliverySucceeds_returnsDeliveryResultSuccess() {
        SendEmailNotificationCommand cmd = SendEmailNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientEmail("test@example.com")
                .subject("Welcome")
                .body("Hello, welcome to Firefly!")
                .build();

        when(deliveryProperties.getDefaultSenderEmail()).thenReturn("noreply@firefly.dev");

        var deliveryResponse = org.fireflyframework.notifications.interfaces.dtos.email.v1.EmailResponseDTO
                .success("provider-msg-id");
        when(emailProvider.sendEmail(any())).thenReturn(Mono.just(deliveryResponse));

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
        SendEmailNotificationCommand cmd = SendEmailNotificationCommand.builder()
                .partyId(UUID.randomUUID())
                .recipientEmail("test@example.com")
                .subject("Welcome")
                .body("Hello!")
                .build();

        when(deliveryProperties.getDefaultSenderEmail()).thenReturn("noreply@firefly.dev");
        when(emailProvider.sendEmail(any()))
                .thenReturn(Mono.error(new RuntimeException("SMTP unavailable")));

        StepVerifier.create(handler.doHandle(cmd))
                .assertNext(result -> {
                    assertThat(result.isDelivered()).isFalse();
                    assertThat(result.getErrorMessage()).isEqualTo("SMTP unavailable");
                    assertThat(result.getMessageId()).isNull();
                })
                .verifyComplete();
    }
}
