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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command to send multiple notifications in bulk.
 * <p>
 * Wraps a list of {@link SendNotificationCommand} instances that are
 * dispatched sequentially through the notification saga.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendBulkNotificationCommand {

    /** List of individual notification commands to dispatch. Must not be empty. */
    @NotEmpty
    @Valid
    private List<SendNotificationCommand> notifications;
}
