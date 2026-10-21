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


package io.fireflysoftware.domain.common.notifications.core.send.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Value object representing the outcome of a notification delivery attempt.
 * Flows from delivery saga steps to audit steps via {@code @FromStep},
 * making delivery success/failure visible to the saga engine independently
 * of audit outcome.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResult implements Serializable {

    private boolean delivered;
    private String messageId;
    private String errorMessage;

    public static DeliveryResult success(String messageId) {
        return DeliveryResult.builder()
                .delivered(true)
                .messageId(messageId)
                .build();
    }

    public static DeliveryResult failed(String errorMessage) {
        return DeliveryResult.builder()
                .delivered(false)
                .errorMessage(errorMessage)
                .build();
    }
}
