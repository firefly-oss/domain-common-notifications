package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.queries.GetPreferencesQuery;
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
