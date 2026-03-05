package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.MarkNotificationReadCommand;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

class MarkNotificationReadHandlerTest {

    private final MarkNotificationReadHandler handler = new MarkNotificationReadHandler();

    @Test
    void doHandle_completesSuccessfully() {
        var cmd = MarkNotificationReadCommand.builder()
                .notificationId(UUID.randomUUID())
                .partyId(UUID.randomUUID())
                .build();

        StepVerifier.create(handler.doHandle(cmd))
                .verifyComplete();
    }
}
