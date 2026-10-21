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


package io.fireflysoftware.domain.common.notifications.core.send;

import java.util.UUID;

/**
 * Raised when an inbox operation addresses a message this service did not send for the party.
 */
public class NotificationNotFoundException extends RuntimeException {

    /**
     * @param notificationId message the caller asked for
     */
    public NotificationNotFoundException(UUID notificationId) {
        super("Notification " + notificationId + " was not found");
    }
}
