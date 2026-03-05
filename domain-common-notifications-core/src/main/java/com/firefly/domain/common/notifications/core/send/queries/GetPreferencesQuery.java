package com.firefly.domain.common.notifications.core.send.queries;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to retrieve notification preferences for a party.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPreferencesQuery implements Query<com.firefly.domain.common.notifications.core.send.model.NotificationPreferencesDTO> {

    /** Unique identifier of the party. */
    @NotNull
    private UUID partyId;
}
