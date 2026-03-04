package com.firefly.domain.common.notifications.infra;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Delivery-related configuration for outbound notifications.
 * <p>
 * Binds values from the {@code firefly.domain.notifications.delivery} prefix.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "firefly.domain.notifications.delivery")
@Data
public class NotificationDeliveryProperties {

    /** Default sender email address used as the {@code from} field in email requests. */
    private String defaultSenderEmail;
}
