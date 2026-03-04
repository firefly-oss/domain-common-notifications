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
 * CQRS command for delivering an email notification via the framework provider.
 * Returns a {@link DeliveryResult} indicating whether the email was
 * successfully delivered, decoupled from audit recording.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailNotificationCommand implements Command<DeliveryResult> {

    /** Unique identifier of the party receiving the email. */
    @NotNull
    private UUID partyId;

    /** Recipient email address. */
    @NotBlank
    private String recipientEmail;

    /** Email subject line. */
    @NotBlank
    private String subject;

    /** Email body content. */
    private String body;

    /** Template code for the email notification. */
    @NotBlank
    private String templateCode;
}
