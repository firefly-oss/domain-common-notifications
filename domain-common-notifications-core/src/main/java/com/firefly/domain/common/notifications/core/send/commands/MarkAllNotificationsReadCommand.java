package com.firefly.domain.common.notifications.core.send.commands;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * Command to mark all notifications for a party as read.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkAllNotificationsReadCommand implements Command<Void> {

    /** Unique identifier of the party. */
    @NotNull
    private UUID partyId;
}
