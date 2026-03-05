package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.MarkAllNotificationsReadCommand;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

class MarkAllNotificationsReadHandlerTest {

    private final MarkAllNotificationsReadHandler handler = new MarkAllNotificationsReadHandler();

    @Test
    void doHandle_completesSuccessfully() {
        var cmd = MarkAllNotificationsReadCommand.builder()
                .partyId(UUID.randomUUID())
                .build();

        StepVerifier.create(handler.doHandle(cmd))
                .verifyComplete();
    }
}
