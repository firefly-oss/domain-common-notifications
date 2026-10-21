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

import io.fireflysoftware.domain.common.notifications.core.send.queries.GetNotificationsByPartyQuery;
import io.fireflysoftware.domain.common.notifications.core.send.store.SentNotificationStore;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetNotificationsByPartyHandlerTest {

    private final SentNotificationStore store = new SentNotificationStore();
    private final GetNotificationsByPartyHandler handler = new GetNotificationsByPartyHandler(store);

    @Test
    void doHandle_returnsMessagesRecordedForTheParty() {
        UUID partyId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        store.recordSent(notificationId, partyId, "EMAIL", "Welcome", "Hello");

        var query = GetNotificationsByPartyQuery.builder()
                .partyId(partyId)
                .page(0)
                .size(20)
                .build();

        StepVerifier.create(handler.doHandle(query))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getNotificationId()).isEqualTo(notificationId);
                    assertThat(list.get(0).getSubject()).isEqualTo("Welcome");
                })
                .verifyComplete();
    }

    @Test
    void doHandle_returnsEmptyListWhenPartyHasNoMessages() {
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
