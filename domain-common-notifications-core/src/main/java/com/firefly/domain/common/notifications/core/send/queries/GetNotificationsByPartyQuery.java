package com.firefly.domain.common.notifications.core.send.queries;

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
public class GetNotificationsByPartyQuery implements Query<List<com.firefly.domain.common.notifications.core.send.model.NotificationDTO>> {

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
