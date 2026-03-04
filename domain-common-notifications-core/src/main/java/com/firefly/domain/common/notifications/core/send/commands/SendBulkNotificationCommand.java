package com.firefly.domain.common.notifications.core.send.commands;

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
