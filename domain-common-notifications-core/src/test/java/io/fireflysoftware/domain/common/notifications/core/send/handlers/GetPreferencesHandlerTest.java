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


package io.fireflysoftware.domain.common.notifications.core.send.handlers;

import io.fireflysoftware.domain.common.notifications.core.send.queries.GetPreferencesQuery;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetPreferencesHandlerTest {

    private final GetPreferencesHandler handler = new GetPreferencesHandler();

    @Test
    void doHandle_returnsDefaultPreferences() {
        UUID partyId = UUID.randomUUID();
        var query = GetPreferencesQuery.builder()
                .partyId(partyId)
                .build();

        StepVerifier.create(handler.doHandle(query))
                .assertNext(prefs -> {
                    assertThat(prefs.getPartyId()).isEqualTo(partyId);
                    assertThat(prefs.getPreferences()).containsEntry("EMAIL", true);
                    assertThat(prefs.getPreferences()).containsEntry("SMS", true);
                    assertThat(prefs.getPreferences()).containsEntry("PUSH", true);
                })
                .verifyComplete();
    }
}
