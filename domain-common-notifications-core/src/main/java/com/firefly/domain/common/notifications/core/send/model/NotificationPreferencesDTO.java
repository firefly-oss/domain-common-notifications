package com.firefly.domain.common.notifications.core.send.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Notification preferences for a party.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferencesDTO {

    private UUID partyId;

    /** Channel preferences (e.g., "EMAIL" -> true, "SMS" -> false). */
    private Map<String, Boolean> preferences;
}
