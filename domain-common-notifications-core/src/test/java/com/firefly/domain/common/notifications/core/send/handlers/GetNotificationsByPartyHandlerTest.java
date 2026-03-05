package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.queries.GetNotificationsByPartyQuery;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetNotificationsByPartyHandlerTest {

    private final GetNotificationsByPartyHandler handler = new GetNotificationsByPartyHandler();

    @Test
    void doHandle_returnsEmptyList() {
        var query = GetNotificationsByPartyQuery.builder()
                .partyId(UUID.randomUUID())
                .page(0)
                .size(20)
                .build();

        StepVerifier.create(handler.doHandle(query))
                .assertNext(list -> assertThat(list).isEmpty())
                .verifyComplete();
    }
}
