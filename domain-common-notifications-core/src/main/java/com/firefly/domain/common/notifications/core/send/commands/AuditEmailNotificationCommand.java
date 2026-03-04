package com.firefly.domain.common.notifications.core.send.commands;

import com.firefly.domain.common.notifications.core.send.model.DeliveryResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * CQRS command for recording an email notification in the core audit database.
 * Carries the original notification details and the {@link DeliveryResult}
 * from the delivery step.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditEmailNotificationCommand implements Command<UUID> {

    @NotNull
    private UUID partyId;

    @NotBlank
    private String recipientEmail;

    @NotBlank
    private String subject;

    private String body;

    @NotNull
    private DeliveryResult deliveryResult;
}
