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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Command to send a notification through the specified channel.
 * <p>
 * Supports EMAIL, SMS, PUSH, and AUTO (automatic channel resolution).
 * When the channel is {@code AUTO}, the system selects the channel based
 * on available recipient contact information.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendNotificationCommand {

    /** Unique identifier of the party receiving the notification. */
    @NotNull
    private UUID partyId;

    /** Delivery channel: EMAIL, SMS, PUSH, or AUTO. */
    @NotBlank
    private String channel;

    /** Template code identifying the notification template (e.g., "ONBOARDING_WELCOME"). */
    @NotBlank
    private String templateCode;

    /** Subject line for email notifications. */
    private String subject;

    /** Body content of the notification. */
    private String body;

    /** Recipient email address for email-channel notifications. */
    private String recipientEmail;

    /** Recipient phone number for SMS-channel notifications. */
    private String recipientPhone;

    /** Additional key-value metadata attached to the notification. */
    private Map<String, String> metadata;
}
