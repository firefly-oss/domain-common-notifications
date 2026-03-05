package com.firefly.domain.common.notifications.web.controllers;

import com.firefly.domain.common.notifications.core.send.commands.*;
import com.firefly.domain.common.notifications.core.send.model.NotificationDTO;
import com.firefly.domain.common.notifications.core.send.queries.GetNotificationDetailQuery;
import com.firefly.domain.common.notifications.core.send.queries.GetNotificationsByPartyQuery;
import com.firefly.domain.common.notifications.core.send.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock private NotificationService notificationService;
    @Mock private CommandBus commandBus;
    @Mock private QueryBus queryBus;

    private NotificationController controller;

    @BeforeEach
    void setUp() {
        controller = new NotificationController(notificationService, commandBus, queryBus);
    }

    @Test
    void getNotificationsForParty_returnsOk() {
        UUID partyId = UUID.randomUUID();
        when(queryBus.query(any(GetNotificationsByPartyQuery.class)))
                .thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(controller.getNotificationsForParty(partyId, 0, 20))
                .assertNext(response -> {
                    assertThat(response.getStatusCode().value()).isEqualTo(200);
                    assertThat(response.getBody()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void getNotificationDetail_returnsOk() {
        UUID notificationId = UUID.randomUUID();
        var dto = NotificationDTO.builder()
                .notificationId(notificationId)
                .channel("EMAIL")
                .build();
        when(queryBus.query(any(GetNotificationDetailQuery.class)))
                .thenReturn(Mono.just(dto));

        StepVerifier.create(controller.getNotificationDetail(notificationId))
                .assertNext(response -> {
                    assertThat(response.getStatusCode().value()).isEqualTo(200);
                    assertThat(response.getBody().getNotificationId()).isEqualTo(notificationId);
                })
                .verifyComplete();
    }

    @Test
    void getNotificationDetail_notFound_returns404() {
        when(queryBus.query(any(GetNotificationDetailQuery.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.getNotificationDetail(UUID.randomUUID()))
                .assertNext(response -> assertThat(response.getStatusCode().value()).isEqualTo(404))
                .verifyComplete();
    }

    @Test
    void markAsRead_returnsOk() {
        when(commandBus.send(any(MarkNotificationReadCommand.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.markAsRead(UUID.randomUUID(), UUID.randomUUID()))
                .assertNext(response -> assertThat(response.getStatusCode().value()).isEqualTo(200))
                .verifyComplete();
    }

    @Test
    void markAllAsRead_returnsOk() {
        when(commandBus.send(any(MarkAllNotificationsReadCommand.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.markAllAsRead(UUID.randomUUID()))
                .assertNext(response -> assertThat(response.getStatusCode().value()).isEqualTo(200))
                .verifyComplete();
    }

    @Test
    void deleteNotification_returnsOk() {
        when(commandBus.send(any(DeleteNotificationCommand.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.deleteNotification(UUID.randomUUID(), UUID.randomUUID()))
                .assertNext(response -> assertThat(response.getStatusCode().value()).isEqualTo(200))
                .verifyComplete();
    }
}
