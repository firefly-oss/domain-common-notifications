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


package io.fireflysoftware.domain.common.notifications.core.send.commands;

import io.fireflysoftware.domain.common.notifications.core.send.model.DeliveryResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * CQRS command for recording an SMS notification in the core audit database.
 * Carries the original notification details and the {@link DeliveryResult}
 * from the delivery step.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditSmsNotificationCommand implements Command<UUID> {

    @NotNull
    private UUID partyId;

    @NotBlank
    private String recipientPhone;

    @NotBlank
    private String message;

    @NotNull
    private DeliveryResult deliveryResult;
}
