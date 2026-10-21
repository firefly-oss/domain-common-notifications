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


package io.fireflysoftware.domain.common.notifications.core.send.queries;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.query.Query;

import java.util.List;
import java.util.UUID;

/**
 * Query to retrieve paginated notifications for a party.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetNotificationsByPartyQuery implements Query<List<io.fireflysoftware.domain.common.notifications.core.send.model.NotificationDTO>> {

    /** Unique identifier of the party. */
    @NotNull
    private UUID partyId;

    /** Page number (zero-based). */
    @Min(0)
    @Builder.Default
    private int page = 0;

    /** Page size. */
    @Min(1)
    @Builder.Default
    private int size = 20;
}
