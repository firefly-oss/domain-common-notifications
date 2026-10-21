/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.domain.common.notifications.infra;

import io.fireflysoftware.common.platform.notification.services.sdk.api.EmailNotificationsApi;
import io.fireflysoftware.common.platform.notification.services.sdk.api.SmsNotificationsApi;
import io.fireflysoftware.common.platform.notification.services.sdk.invoker.ApiClient;
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
