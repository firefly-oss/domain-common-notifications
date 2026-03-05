package com.firefly.domain.common.notifications.core.send.commands;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.command.Command;

import java.util.Map;
import java.util.UUID;

/**
 * Command to update notification preferences for a party.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePreferencesCommand implements Command<Void> {

    /** Unique identifier of the party. */
    @NotNull
    private UUID partyId;

    /** Channel preferences (e.g., "EMAIL" -> true, "SMS" -> false). */
    @NotNull
    private Map<String, Boolean> preferences;
}
