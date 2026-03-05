package com.firefly.domain.common.notifications.web.controllers;

import com.firefly.domain.common.notifications.core.send.commands.*;
import com.firefly.domain.common.notifications.core.send.model.NotificationDTO;
import com.firefly.domain.common.notifications.core.send.queries.GetNotificationDetailQuery;
import com.firefly.domain.common.notifications.core.send.queries.GetNotificationsByPartyQuery;
import com.firefly.domain.common.notifications.core.send.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.cqrs.query.QueryBus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for multi-channel notification orchestration.
 * <p>
 * Provides endpoints for sending notifications (single and bulk),
 * retrieving notification history, managing read status, and deleting notifications.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification orchestration endpoints")
public class NotificationController {

    private final NotificationService notificationService;
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    /**
     * Sends a single notification through the appropriate channel.
     *
     * @param command the notification command containing channel, recipient and content details
     * @return an empty 200 response on successful dispatch
     */
    @PostMapping("/send")
    @Operation(summary = "Send notification", description = "Send a single notification through the appropriate channel")
    @ApiResponse(responseCode = "200", description = "Notification dispatched successfully")
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
    @ApiResponse(responseCode = "200", description = "All notifications dispatched successfully")
    public Mono<ResponseEntity<Object>> sendBulkNotifications(@Valid @RequestBody SendBulkNotificationCommand command) {
        return notificationService.sendBulkNotifications(command)
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * Retrieves all notifications for the given party.
     *
     * @param partyId the unique identifier of the party
     * @param page    page number (zero-based)
     * @param size    page size
     * @return the list of notifications belonging to the party
     */
    @GetMapping("/party/{partyId}")
    @Operation(summary = "Get notifications for party", description = "Retrieve all notifications for a given party")
    @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully")
    public Mono<ResponseEntity<List<NotificationDTO>>> getNotificationsForParty(
            @PathVariable UUID partyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return queryBus.query(GetNotificationsByPartyQuery.builder()
                        .partyId(partyId)
                        .page(page)
                        .size(size)
                        .build())
                .map(ResponseEntity::ok);
    }

    /**
     * Retrieves the detail of a specific notification.
     *
     * @param notificationId the unique identifier of the notification
     * @return the notification detail
     */
    @GetMapping("/{notificationId}")
    @Operation(summary = "Get notification detail", description = "Retrieve details of a specific notification")
    @ApiResponse(responseCode = "200", description = "Notification detail retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    public Mono<ResponseEntity<NotificationDTO>> getNotificationDetail(@PathVariable UUID notificationId) {
        return queryBus.query(GetNotificationDetailQuery.builder()
                        .notificationId(notificationId)
                        .build())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId the unique identifier of the notification to mark as read
     * @param partyId        the party owning the notification
     * @return an empty 200 response on success
     */
    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark notification as read", description = "Mark a specific notification as read")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    public Mono<ResponseEntity<Object>> markAsRead(
            @PathVariable UUID notificationId,
            @RequestParam UUID partyId) {
        return commandBus.send(MarkNotificationReadCommand.builder()
                        .notificationId(notificationId)
                        .partyId(partyId)
                        .build())
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * Marks all notifications for a party as read.
     *
     * @param partyId the unique identifier of the party
     * @return an empty 200 response on success
     */
    @PostMapping("/party/{partyId}/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Mark all notifications for a party as read")
    @ApiResponse(responseCode = "200", description = "All notifications marked as read")
    public Mono<ResponseEntity<Object>> markAllAsRead(@PathVariable UUID partyId) {
        return commandBus.send(MarkAllNotificationsReadCommand.builder()
                        .partyId(partyId)
                        .build())
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * Deletes a specific notification.
     *
     * @param notificationId the unique identifier of the notification to delete
     * @param partyId        the party owning the notification
     * @return an empty 200 response on success
     */
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete notification", description = "Delete a specific notification")
    @ApiResponse(responseCode = "200", description = "Notification deleted")
    public Mono<ResponseEntity<Object>> deleteNotification(
            @PathVariable UUID notificationId,
            @RequestParam UUID partyId) {
        return commandBus.send(DeleteNotificationCommand.builder()
                        .notificationId(notificationId)
                        .partyId(partyId)
                        .build())
                .thenReturn(ResponseEntity.ok().build());
    }
}
