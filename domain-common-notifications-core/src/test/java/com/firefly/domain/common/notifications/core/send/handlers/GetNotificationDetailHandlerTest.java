package com.firefly.domain.common.notifications.core.send.handlers;

import com.firefly.domain.common.notifications.core.send.queries.GetNotificationDetailQuery;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

class GetNotificationDetailHandlerTest {

    private final GetNotificationDetailHandler handler = new GetNotificationDetailHandler();

    @Test
    void doHandle_returnsEmpty() {
        var query = GetNotificationDetailQuery.builder()
                .notificationId(UUID.randomUUID())
                .build();

        StepVerifier.create(handler.doHandle(query))
                .verifyComplete();
    }
}
