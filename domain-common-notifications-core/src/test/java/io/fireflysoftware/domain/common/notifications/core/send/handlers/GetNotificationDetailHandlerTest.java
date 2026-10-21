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

import io.fireflysoftware.domain.common.notifications.core.send.queries.GetNotificationDetailQuery;
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
