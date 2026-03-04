package com.firefly.domain.common.notifications.web.controllers;

import com.firefly.domain.common.notifications.core.send.commands.SendBulkNotificationCommand;
import com.firefly.domain.common.notifications.core.send.commands.SendNotificationCommand;
import com.firefly.domain.common.notifications.core.send.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fireflyframework.web.error.exceptions.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller for multi-channel notification orchestration.
 * <p>
 * Provides endpoints for sending notifications (single and bulk),
 * retrieving notification history, managing read status, and
 * configuring per-party notification preferences.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification orchestration endpoints")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Sends a single notification through the appropriate channel.
     *
     * @param command the notification command containing channel, recipient and content details
     * @return an empty 200 response on successful dispatch
     */
    @PostMapping("/send")
    @Operation(summary = "Send notification", description = "Send a single notification through the appropriate channel")
    public Mono<ResponseEntity<Object>> sendNotification(@Valid @RequestBody SendNotificationCommand command) {
        return notificationService.sendNotification(command)
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * Sends multiple notifications in bulk.
     *
     * @param command the bulk notification command containing a list of individual notifications
     * @return an empty 200 response on successful dispatch of all notifications
     */
    @PostMapping("/send-bulk")
    @Operation(summary = "Send bulk notifications", description = "Send multiple notifications in bulk")
    public Mono<ResponseEntity<Object>> sendBulkNotifications(@Valid @RequestBody SendBulkNotificationCommand command) {
        return notificationService.sendBulkNotifications(command)
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * Retrieves all notifications for the given party.
     *
     * @param partyId the unique identifier of the party
     * @return the list of notifications belonging to the party
     */
    @GetMapping("/party/{partyId}")
    @Operation(summary = "Get notifications for party", description = "Retrieve all notifications for a given party")
    public Mono<ResponseEntity<Object>> getNotificationsForParty(@PathVariable UUID partyId) {
        return Mono.error(new NotImplementedException("NOTIFICATION_NOT_IMPL", "Endpoint not yet implemented"));
    }

    /**
     * Retrieves the detail of a specific notification.
     *
     * @param notificationId the unique identifier of the notification
     * @return the notification detail
     */
    @GetMapping("/{notificationId}")
    @Operation(summary = "Get notification detail", description = "Retrieve details of a specific notification")
    public Mono<ResponseEntity<Object>> getNotificationDetail(@PathVariable UUID notificationId) {
        return Mono.error(new NotImplementedException("NOTIFICATION_NOT_IMPL", "Endpoint not yet implemented"));
    }

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId the unique identifier of the notification to mark as read
     * @return an empty 200 response on success
     */
    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark notification as read", description = "Mark a specific notification as read")
    public Mono<ResponseEntity<Object>> markAsRead(@PathVariable UUID notificationId) {
        return Mono.error(new NotImplementedException("NOTIFICATION_NOT_IMPL", "Endpoint not yet implemented"));
    }

    /**
     * Marks all notifications for a party as read.
     *
     * @param partyId the unique identifier of the party
     * @return an empty 200 response on success
     */
    @PostMapping("/party/{partyId}/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Mark all notifications for a party as read")
    public Mono<ResponseEntity<Object>> markAllAsRead(@PathVariable UUID partyId) {
        return Mono.error(new NotImplementedException("NOTIFICATION_NOT_IMPL", "Endpoint not yet implemented"));
    }

    /**
     * Deletes a specific notification.
     *
     * @param notificationId the unique identifier of the notification to delete
     * @return an empty 200 response on success
     */
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete notification", description = "Delete a specific notification")
    public Mono<ResponseEntity<Object>> deleteNotification(@PathVariable UUID notificationId) {
        return Mono.error(new NotImplementedException("NOTIFICATION_NOT_IMPL", "Endpoint not yet implemented"));
    }

    /**
     * Retrieves notification preferences for a party.
     *
     * @param partyId the unique identifier of the party
     * @return the notification preferences for the party
     */
    @GetMapping("/preferences/{partyId}")
    @Operation(summary = "Get notification preferences", description = "Retrieve notification preferences for a party")
    public Mono<ResponseEntity<Object>> getPreferences(@PathVariable UUID partyId) {
        return Mono.error(new NotImplementedException("NOTIFICATION_NOT_IMPL", "Endpoint not yet implemented"));
    }

    /**
     * Updates notification preferences for a party.
     *
     * @param partyId the unique identifier of the party
     * @return an empty 200 response on success
     */
    @PutMapping("/preferences/{partyId}")
    @Operation(summary = "Update notification preferences", description = "Update notification preferences for a party")
    public Mono<ResponseEntity<Object>> updatePreferences(@PathVariable UUID partyId) {
        return Mono.error(new NotImplementedException("NOTIFICATION_NOT_IMPL", "Endpoint not yet implemented"));
    }
}
