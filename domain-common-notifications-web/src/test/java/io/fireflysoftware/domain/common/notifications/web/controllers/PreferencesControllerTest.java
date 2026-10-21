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


package io.fireflysoftware.domain.common.notifications.web.controllers;

import io.fireflysoftware.domain.common.notifications.core.send.commands.UpdatePreferencesCommand;
import io.fireflysoftware.domain.common.notifications.core.send.model.NotificationPreferencesDTO;
import io.fireflysoftware.domain.common.notifications.core.send.queries.GetPreferencesQuery;
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
