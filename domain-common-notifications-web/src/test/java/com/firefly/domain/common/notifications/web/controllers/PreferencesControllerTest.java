package com.firefly.domain.common.notifications.web.controllers;

import com.firefly.domain.common.notifications.core.send.commands.UpdatePreferencesCommand;
import com.firefly.domain.common.notifications.core.send.model.NotificationPreferencesDTO;
import com.firefly.domain.common.notifications.core.send.queries.GetPreferencesQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PreferencesControllerTest {

    @Mock private CommandBus commandBus;
    @Mock private QueryBus queryBus;

    private PreferencesController controller;

    @BeforeEach
    void setUp() {
        controller = new PreferencesController(commandBus, queryBus);
    }

    @Test
    void getPreferences_returnsOk() {
        UUID partyId = UUID.randomUUID();
        var prefs = NotificationPreferencesDTO.builder()
                .partyId(partyId)
                .preferences(Map.of("EMAIL", true, "SMS", true))
                .build();
        when(queryBus.query(any(GetPreferencesQuery.class)))
                .thenReturn(Mono.just(prefs));

        StepVerifier.create(controller.getPreferences(partyId))
                .assertNext(response -> {
                    assertThat(response.getStatusCode().value()).isEqualTo(200);
                    assertThat(response.getBody().getPartyId()).isEqualTo(partyId);
                })
                .verifyComplete();
    }

    @Test
    void updatePreferences_returnsOk() {
        when(commandBus.send(any(UpdatePreferencesCommand.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.updatePreferences(UUID.randomUUID(), Map.of("EMAIL", false)))
                .assertNext(response -> assertThat(response.getStatusCode().value()).isEqualTo(200))
                .verifyComplete();
    }
}
