package com.firefly.domain.common.notifications.core.utils.constants;

/**
 * Constants for notification saga orchestration.
 * Includes saga names, step identifiers, compensation method names,
 * event types, and execution-context variable keys used throughout
 * the notification domain.
 */
public final class NotificationConstants {

    private NotificationConstants() {
    }

    // Saga names

    public static final String SAGA_SEND_NOTIFICATION = "SendNotificationSaga";
    public static final String SAGA_SEND_BULK         = "SendBulkNotificationSaga";

    // Step IDs

    public static final String STEP_ROUTE_NOTIFICATION = "routeNotification";
    public static final String STEP_DELIVER_EMAIL      = "deliverEmail";
    public static final String STEP_DELIVER_SMS        = "deliverSms";
    public static final String STEP_AUDIT_EMAIL        = "auditEmail";
    public static final String STEP_AUDIT_SMS          = "auditSms";

    // Compensation

    public static final String COMPENSATE_DELIVER_EMAIL = "compensateDeliverEmail";
    public static final String COMPENSATE_DELIVER_SMS   = "compensateDeliverSms";
    public static final String COMPENSATE_AUDIT_EMAIL   = "compensateAuditEmail";
    public static final String COMPENSATE_AUDIT_SMS     = "compensateAuditSms";

    // Events

    public static final String EVENT_NOTIFICATION_DELIVERED = "notification.delivered";
    public static final String EVENT_NOTIFICATION_SENT      = "notification.sent";
    public static final String EVENT_NOTIFICATION_FAILED    = "notification.failed";

    // Channels

    public static final String CHANNEL_AUTO  = "AUTO";
    public static final String CHANNEL_EMAIL = "EMAIL";
    public static final String CHANNEL_SMS   = "SMS";

    // Context

    public static final String CTX_NOTIFICATION_ID = "notificationId";
    public static final String CTX_CHANNEL         = "channel";
}
