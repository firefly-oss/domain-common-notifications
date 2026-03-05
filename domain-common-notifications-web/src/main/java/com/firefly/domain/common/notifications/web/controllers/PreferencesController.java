package com.firefly.domain.common.notifications.web.controllers;

import com.firefly.domain.common.notifications.core.send.commands.UpdatePreferencesCommand;
import com.firefly.domain.common.notifications.core.send.model.NotificationPreferencesDTO;
import com.firefly.domain.common.notifications.core.send.queries.GetPreferencesQuery;
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

import java.util.Map;
import java.util.UUID;

/**
 * REST controller for managing notification preferences per party.
 */
@RestController
@RequestMapping("/api/v1/notifications/preferences")
@RequiredArgsConstructor
@Tag(name = "Notification Preferences", description = "Notification preference management endpoints")
public class PreferencesController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    /**
     * Retrieves notification preferences for a party.
     *
     * @param partyId the unique identifier of the party
     * @return the notification preferences
     */
    @GetMapping("/{partyId}")
    @Operation(summary = "Get notification preferences", description = "Retrieve notification preferences for a party")
    @ApiResponse(responseCode = "200", description = "Preferences retrieved successfully")
    public Mono<ResponseEntity<NotificationPreferencesDTO>> getPreferences(@PathVariable UUID partyId) {
        return queryBus.query(GetPreferencesQuery.builder()
                        .partyId(partyId)
                        .build())
                .map(ResponseEntity::ok);
    }

    /**
     * Updates notification preferences for a party.
     *
     * @param partyId     the unique identifier of the party
     * @param preferences the channel preferences to update
     * @return an empty 200 response on success
     */
    @PutMapping("/{partyId}")
    @Operation(summary = "Update notification preferences", description = "Update notification preferences for a party")
    @ApiResponse(responseCode = "200", description = "Preferences updated successfully")
    public Mono<ResponseEntity<Object>> updatePreferences(
            @PathVariable UUID partyId,
            @Valid @RequestBody Map<String, Boolean> preferences) {
        return commandBus.send(UpdatePreferencesCommand.builder()
                        .partyId(partyId)
                        .preferences(preferences)
                        .build())
                .thenReturn(ResponseEntity.ok().build());
    }
}
