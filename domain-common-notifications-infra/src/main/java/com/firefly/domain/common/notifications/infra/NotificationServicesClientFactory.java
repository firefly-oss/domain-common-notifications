package com.firefly.domain.common.notifications.infra;

import com.firefly.common.platform.notification.services.sdk.api.EmailNotificationsApi;
import com.firefly.common.platform.notification.services.sdk.api.SmsNotificationsApi;
import com.firefly.common.platform.notification.services.sdk.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Factory that wires the core-common-notification-services SDK.
 * <p>
 * Provides Spring beans for {@link EmailNotificationsApi} and
 * {@link SmsNotificationsApi} that handlers inject.
 * </p>
 */
@Component
public class NotificationServicesClientFactory {

    private final ApiClient apiClient;

    /**
     * Creates the client factory and configures the {@link ApiClient}
     * with the base path from application properties.
     *
     * @param properties the notification services connection properties
     */
    public NotificationServicesClientFactory(NotificationServicesProperties properties) {
        this.apiClient = new ApiClient();
        this.apiClient.setBasePath(properties.getBasePath());
    }

    /**
     * Creates the email notifications API bean.
     *
     * @return a configured {@link EmailNotificationsApi} instance
     */
    @Bean
    public EmailNotificationsApi emailNotificationsApi() {
        return new EmailNotificationsApi(apiClient);
    }

    /**
     * Creates the SMS notifications API bean.
     *
     * @return a configured {@link SmsNotificationsApi} instance
     */
    @Bean
    public SmsNotificationsApi smsNotificationsApi() {
        return new SmsNotificationsApi(apiClient);
    }
}
