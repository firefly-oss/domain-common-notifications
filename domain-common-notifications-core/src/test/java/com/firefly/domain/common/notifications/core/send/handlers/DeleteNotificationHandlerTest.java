package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.DeleteNotificationCommand;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

class DeleteNotificationHandlerTest {

    private final DeleteNotificationHandler handler = new DeleteNotificationHandler();

    @Test
    void doHandle_completesSuccessfully() {
        var cmd = DeleteNotificationCommand.builder()
                .notificationId(UUID.randomUUID())
                .partyId(UUID.randomUUID())
                .build();

        StepVerifier.create(handler.doHandle(cmd))
                .verifyComplete();
    }
}
