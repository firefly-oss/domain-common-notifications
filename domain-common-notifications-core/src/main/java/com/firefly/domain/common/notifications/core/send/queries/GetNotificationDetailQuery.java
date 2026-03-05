package com.firefly.domain.common.notifications.core.send.queries;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.query.Query;

import java.util.UUID;

/**
 * Query to retrieve the detail of a specific notification.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetNotificationDetailQuery implements Query<com.firefly.domain.common.notifications.core.send.model.NotificationDTO> {

    /** Unique identifier of the notification. */
    @NotNull
    private UUID notificationId;
}
