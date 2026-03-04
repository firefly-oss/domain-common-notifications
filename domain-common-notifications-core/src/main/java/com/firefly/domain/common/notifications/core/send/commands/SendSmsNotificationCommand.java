package com.firefly.domain.common.notifications.core.send.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.firefly.domain.common.notifications.core.send.model.DeliveryResult;
import org.fireflyframework.cqrs.command.Command;

import java.util.UUID;

/**
 * CQRS command for delivering an SMS notification via the framework provider.
 * Returns a {@link DeliveryResult} indicating whether the SMS was
 * successfully delivered, decoupled from audit recording.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendSmsNotificationCommand implements Command<DeliveryResult> {

    /** Unique identifier of the party receiving the SMS. */
    @NotNull
    private UUID partyId;

    /** Recipient phone number. */
    @NotBlank
    private String recipientPhone;

    /** SMS message content. */
    @NotBlank
    private String message;

    /** Template code for the SMS notification. */
    @NotBlank
    private String templateCode;
}
