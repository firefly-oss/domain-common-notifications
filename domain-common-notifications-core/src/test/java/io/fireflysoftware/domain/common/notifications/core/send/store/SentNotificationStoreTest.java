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


package io.fireflysoftware.domain.common.notifications.core.send.store;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SentNotificationStoreTest {

    private final SentNotificationStore store = new SentNotificationStore();

    @Test
    void listDetailMarkAndDeleteOperateOnRecordedMessages() {
        UUID partyId = UUID.randomUUID();
        UUID otherParty = UUID.randomUUID();
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();

        store.recordSent(first, partyId, "EMAIL", "Welcome", "Hello");
        store.recordSent(second, partyId, "SMS", null, "Code 1234");
        store.recordSent(UUID.randomUUID(), otherParty, "EMAIL", "Other", "Not yours");

        assertThat(store.listByParty(partyId, 0, 20)).hasSize(2);
        assertThat(store.listByParty(partyId, 0, 1)).hasSize(1);
        assertThat(store.find(first)).get().extracting("subject").isEqualTo("Welcome");
        assertThat(store.find(first).orElseThrow().isRead()).isFalse();

        assertThat(store.markRead(first, otherParty)).isFalse();
        assertThat(store.markRead(first, partyId)).isTrue();
        assertThat(store.find(first).orElseThrow().isRead()).isTrue();
        assertThat(store.find(first).orElseThrow().getReadAt()).isNotNull();

        store.markAllRead(partyId);
        assertThat(store.find(second).orElseThrow().isRead()).isTrue();
        assertThat(store.listByParty(otherParty, 0, 20).get(0).isRead()).isFalse();

        assertThat(store.delete(second, otherParty)).isFalse();
        assertThat(store.delete(second, partyId)).isTrue();
        assertThat(store.find(second)).isEmpty();
        assertThat(store.listByParty(partyId, 0, 20)).extracting("notificationId").containsExactly(first);
    }
}
