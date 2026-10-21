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

import io.fireflysoftware.domain.common.notifications.core.send.commands.MarkAllNotificationsReadCommand;
import io.fireflysoftware.domain.common.notifications.core.send.store.SentNotificationStore;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MarkAllNotificationsReadHandlerTest {

    private final SentNotificationStore store = new SentNotificationStore();
    private final MarkAllNotificationsReadHandler handler = new MarkAllNotificationsReadHandler(store);

    @Test
    void doHandle_marksEveryMessageForTheParty() {
        UUID partyId = UUID.randomUUID();
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        store.recordSent(first, partyId, "EMAIL", "One", "Body");
        store.recordSent(second, partyId, "SMS", null, "Two");

        var cmd = MarkAllNotificationsReadCommand.builder()
                .partyId(partyId)
                .build();

        StepVerifier.create(handler.doHandle(cmd))
                .verifyComplete();

        assertThat(store.find(first).orElseThrow().isRead()).isTrue();
        assertThat(store.find(second).orElseThrow().isRead()).isTrue();
    }
}
