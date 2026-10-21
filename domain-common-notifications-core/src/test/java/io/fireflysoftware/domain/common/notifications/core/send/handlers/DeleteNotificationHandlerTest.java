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

import io.fireflysoftware.domain.common.notifications.core.send.NotificationNotFoundException;
import io.fireflysoftware.domain.common.notifications.core.send.commands.DeleteNotificationCommand;
import io.fireflysoftware.domain.common.notifications.core.send.store.SentNotificationStore;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteNotificationHandlerTest {

    private final SentNotificationStore store = new SentNotificationStore();
    private final DeleteNotificationHandler handler = new DeleteNotificationHandler(store);

    @Test
    void doHandle_removesThePartyMessage() {
        UUID partyId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        store.recordSent(notificationId, partyId, "EMAIL", "Welcome", "Hello");

        var cmd = DeleteNotificationCommand.builder()
                .notificationId(notificationId)
                .partyId(partyId)
                .build();

        StepVerifier.create(handler.doHandle(cmd))
                .verifyComplete();

        assertThat(store.find(notificationId)).isEmpty();
    }

    @Test
    void doHandle_errorsWhenMessageIsMissing() {
        var cmd = DeleteNotificationCommand.builder()
                .notificationId(UUID.randomUUID())
                .partyId(UUID.randomUUID())
                .build();

        StepVerifier.create(handler.doHandle(cmd))
                .expectError(NotificationNotFoundException.class)
                .verify();
    }
}
