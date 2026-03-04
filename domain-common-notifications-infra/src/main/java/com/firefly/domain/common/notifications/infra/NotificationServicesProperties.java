package com.firefly.domain.common.notifications.infra;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the core-common-notification-services SDK connection.
 * <p>
 * Binds values from the {@code api-configuration.common-platform.notification-services}
 * prefix in application configuration.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "api-configuration.common-platform.notification-services")
@Data
public class NotificationServicesProperties {

    /** Base URL of the core-common-notification-services API. */
    private String basePath;
}
