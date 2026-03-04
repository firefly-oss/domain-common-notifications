package com.firefly.domain.common.notifications.core.send.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Value object representing the outcome of a notification delivery attempt.
 * Flows from delivery saga steps to audit steps via {@code @FromStep},
 * making delivery success/failure visible to the saga engine independently
 * of audit outcome.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResult implements Serializable {

    private boolean delivered;
    private String messageId;
    private String errorMessage;

    public static DeliveryResult success(String messageId) {
        return DeliveryResult.builder()
                .delivered(true)
                .messageId(messageId)
                .build();
    }

    public static DeliveryResult failed(String errorMessage) {
        return DeliveryResult.builder()
                .delivered(false)
                .errorMessage(errorMessage)
                .build();
    }
}
