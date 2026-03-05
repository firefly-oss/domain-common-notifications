package com.firefly.domain.common.notifications.core.send.commands;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to mark a specific notification as read.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkNotificationReadCommand implements Command<Void> {

    /** Unique identifier of the notification. */
    @NotNull
    private UUID notificationId;

    /** Unique identifier of the party owning the notification. */
    @NotNull
    private UUID partyId;
}
