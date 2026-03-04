package com.firefly.domain.common.notifications.core.send.workflows;

import com.firefly.domain.common.notifications.core.send.commands.AuditEmailNotificationCommand;
import com.firefly.domain.common.notifications.core.send.commands.AuditSmsNotificationCommand;
import com.firefly.domain.common.notifications.core.send.commands.SendEmailNotificationCommand;
import com.firefly.domain.common.notifications.core.send.commands.SendNotificationCommand;
import com.firefly.domain.common.notifications.core.send.commands.SendSmsNotificationCommand;
import com.firefly.domain.common.notifications.core.send.model.DeliveryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.cqrs.command.CommandBus;
import org.fireflyframework.orchestration.core.argument.FromStep;
import org.fireflyframework.orchestration.core.context.ExecutionContext;
import org.fireflyframework.orchestration.saga.annotation.Saga;
import org.fireflyframework.orchestration.saga.annotation.SagaStep;
import org.fireflyframework.orchestration.saga.annotation.StepEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.firefly.domain.common.notifications.core.utils.constants.NotificationConstants.*;

/**
 * Saga for sending a single notification.
 * Routes to the appropriate channel (email or SMS), delivers via the
 * framework provider, then audits the result via the core notification SDK.
 *
 * <p>DAG topology:
 * <pre>
 * routeNotification (Layer 0)
 *     ├── deliverEmail (Layer 1, retry=3, backoff=2000ms, jitter)
 *     │   └── auditEmail (Layer 2, dependsOn deliverEmail, retry=2, backoff=500ms)
 *     ├── deliverSms (Layer 1, retry=3, backoff=2000ms, jitter)
 *     │   └── auditSms (Layer 2, dependsOn deliverSms, retry=2, backoff=500ms)
 * </pre>
 */
@Saga(name = SAGA_SEND_NOTIFICATION)
@Service
@Slf4j
@RequiredArgsConstructor
public class SendNotificationSaga {

    private final CommandBus commandBus;

    // ── Layer 0: Route ──────────────────────────────────────────────────

    @SagaStep(id = STEP_ROUTE_NOTIFICATION)
    public Mono<String> routeNotification(SendNotificationCommand cmd, ExecutionContext ctx) {
        String channel = cmd.getChannel();
        if (CHANNEL_AUTO.equalsIgnoreCase(channel)) {
            if (cmd.getRecipientEmail() != null && !cmd.getRecipientEmail().isBlank()) {
                channel = CHANNEL_EMAIL;
            } else {
                channel = CHANNEL_SMS;
            }
        }
        ctx.putVariable(CTX_CHANNEL, channel);
        return Mono.just(channel);
    }

    // ── Layer 1: Deliver ────────────────────────────────────────────────

    @SagaStep(id = STEP_DELIVER_EMAIL, dependsOn = STEP_ROUTE_NOTIFICATION,
              compensate = COMPENSATE_DELIVER_EMAIL, retry = 3, backoffMs = 2000, jitter = true)
    @StepEvent(type = EVENT_NOTIFICATION_DELIVERED)
    public Mono<DeliveryResult> deliverEmail(SendNotificationCommand cmd, ExecutionContext ctx) {
        Object channelValue = ctx.getVariable(CTX_CHANNEL);
        String resolvedChannel = channelValue instanceof String s ? s : null;
        if (!CHANNEL_EMAIL.equalsIgnoreCase(resolvedChannel)) {
            return Mono.empty();
        }
        SendEmailNotificationCommand emailCmd = SendEmailNotificationCommand.builder()
                .partyId(cmd.getPartyId())
                .recipientEmail(cmd.getRecipientEmail())
                .subject(cmd.getSubject())
                .body(cmd.getBody())
                .templateCode(cmd.getTemplateCode())
                .build();
        return commandBus.send(emailCmd);
    }

    public Mono<Void> compensateDeliverEmail(DeliveryResult delivery) {
        log.warn("Email delivery cannot be reversed -- logging compensation (delivered={})",
                delivery != null ? delivery.isDelivered() : "null");
        return Mono.empty();
    }

    @SagaStep(id = STEP_DELIVER_SMS, dependsOn = STEP_ROUTE_NOTIFICATION,
              compensate = COMPENSATE_DELIVER_SMS, retry = 3, backoffMs = 2000, jitter = true)
    @StepEvent(type = EVENT_NOTIFICATION_DELIVERED)
    public Mono<DeliveryResult> deliverSms(SendNotificationCommand cmd, ExecutionContext ctx) {
        Object channelValue = ctx.getVariable(CTX_CHANNEL);
        String resolvedChannel = channelValue instanceof String s ? s : null;
        if (!CHANNEL_SMS.equalsIgnoreCase(resolvedChannel)) {
            return Mono.empty();
        }
        SendSmsNotificationCommand smsCmd = SendSmsNotificationCommand.builder()
                .partyId(cmd.getPartyId())
                .recipientPhone(cmd.getRecipientPhone())
                .message(cmd.getBody())
                .templateCode(cmd.getTemplateCode())
                .build();
        return commandBus.send(smsCmd);
    }

    public Mono<Void> compensateDeliverSms(DeliveryResult delivery) {
        log.warn("SMS delivery cannot be reversed -- logging compensation (delivered={})",
                delivery != null ? delivery.isDelivered() : "null");
        return Mono.empty();
    }

    // ── Layer 2: Audit ──────────────────────────────────────────────────

    @SagaStep(id = STEP_AUDIT_EMAIL, dependsOn = STEP_DELIVER_EMAIL,
              compensate = COMPENSATE_AUDIT_EMAIL, retry = 2, backoffMs = 500)
    @StepEvent(type = EVENT_NOTIFICATION_SENT)
    public Mono<UUID> auditEmail(SendNotificationCommand cmd,
                                 @FromStep(STEP_DELIVER_EMAIL) DeliveryResult delivery,
                                 ExecutionContext ctx) {
        if (delivery == null) {
            return Mono.empty();
        }
        AuditEmailNotificationCommand auditCmd = AuditEmailNotificationCommand.builder()
                .partyId(cmd.getPartyId())
                .recipientEmail(cmd.getRecipientEmail())
                .subject(cmd.getSubject())
                .body(cmd.getBody())
                .deliveryResult(delivery)
                .build();
        return commandBus.<UUID>send(auditCmd)
                .doOnNext(notificationId -> ctx.putVariable(CTX_NOTIFICATION_ID, notificationId));
    }

    public Mono<Void> compensateAuditEmail(UUID notificationId) {
        log.warn("Orphaned email audit record {} -- manual cleanup may be required", notificationId);
        return Mono.empty();
    }

    @SagaStep(id = STEP_AUDIT_SMS, dependsOn = STEP_DELIVER_SMS,
              compensate = COMPENSATE_AUDIT_SMS, retry = 2, backoffMs = 500)
    @StepEvent(type = EVENT_NOTIFICATION_SENT)
    public Mono<UUID> auditSms(SendNotificationCommand cmd,
                               @FromStep(STEP_DELIVER_SMS) DeliveryResult delivery,
                               ExecutionContext ctx) {
        if (delivery == null) {
            return Mono.empty();
        }
        AuditSmsNotificationCommand auditCmd = AuditSmsNotificationCommand.builder()
                .partyId(cmd.getPartyId())
                .recipientPhone(cmd.getRecipientPhone())
                .message(cmd.getBody())
                .deliveryResult(delivery)
                .build();
        return commandBus.<UUID>send(auditCmd)
                .doOnNext(notificationId -> ctx.putVariable(CTX_NOTIFICATION_ID, notificationId));
    }

    public Mono<Void> compensateAuditSms(UUID notificationId) {
        log.warn("Orphaned SMS audit record {} -- manual cleanup may be required", notificationId);
        return Mono.empty();
    }
}
