package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.commands.UpdatePreferencesCommand;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;

class UpdatePreferencesHandlerTest {

    private final UpdatePreferencesHandler handler = new UpdatePreferencesHandler();

    @Test
    void doHandle_completesSuccessfully() {
        var cmd = UpdatePreferencesCommand.builder()
                .partyId(UUID.randomUUID())
                .preferences(Map.of("EMAIL", true, "SMS", false))
                .build();

        StepVerifier.create(handler.doHandle(cmd))
                .verifyComplete();
    }
}
