package distributed_sota.payment_service.domain.model;

import common.ddd.Aggregate;
import distributed_sota.payment_service.domain.event.*;

import java.time.Instant;
import java.util.Objects;

public class Payment implements Aggregate<PaymentId> {

    private final PaymentId paymentId;
    private final String userId;
    private final String deliveryId;
    private final Amount amount;
    private final Instant whenCreated;

    private PaymentState state;

    private Payment(PaymentId paymentId, String userId, String deliveryId, Amount amount) {
        this(paymentId, userId, deliveryId, amount, Instant.now(), PaymentState.PAYMENT_PENDING);
    }

    private Payment(
            PaymentId paymentId,
            String userId,
            String deliveryId,
            Amount amount,
            Instant whenCreated,
            PaymentState state
    ) {
        this.paymentId = Objects.requireNonNull(paymentId);
        this.userId = Objects.requireNonNull(userId);
        this.deliveryId = Objects.requireNonNull(deliveryId);
        this.amount = Objects.requireNonNull(amount);
        this.whenCreated = Objects.requireNonNull(whenCreated);
        this.state = Objects.requireNonNull(state);
    }

    public static Payment create(PaymentId id, String userId, String deliveryId, Amount amount) {
        return new Payment(id, userId, deliveryId, amount);
    }

    public static Payment rehydrate(
            PaymentId id,
            String userId,
            String deliveryId,
            Amount amount,
            Instant whenCreated,
            PaymentState state
    ) {
        return new Payment(id, userId, deliveryId, amount, whenCreated, state);
    }

    public PaymentId getPaymentId() { return paymentId; }
    public String getUserId() { return userId; }
    public String getDeliveryId() { return deliveryId; }
    public Amount getAmount() { return amount; }
    public Instant getWhenCreated() { return whenCreated; }
    public PaymentState getState() { return state; }

    // Domain actions return domain events (to be published)
    public PaymentInitiatedEvent markPaymentInitiated() {
        if (state != PaymentState.PAYMENT_PENDING) {
            throw new IllegalStateException("Payment cannot be initiated in state: " + state);
        }
        // state stays pending while processing
        return PaymentInitiatedEvent.from(this);
    }

    public PaymentSucceededEvent markPaymentConfirmed() {
        if (state != PaymentState.PAYMENT_PENDING) {
            throw new IllegalStateException("Payment cannot be confirmed in state: " + state);
        }
        this.state = PaymentState.PAYMENT_CONFIRMED;
        return PaymentSucceededEvent.from(this);
    }

    public PaymentFailedEvent markPaymentFailed(String reason) {
        if (state != PaymentState.PAYMENT_PENDING) {
            throw new IllegalStateException("Payment cannot fail in state: " + state);
        }
        this.state = PaymentState.PAYMENT_FAILED;
        return PaymentFailedEvent.from(this, reason);
    }

    public RefundIssuedEvent markRefundIssued() {
        if (state != PaymentState.PAYMENT_CONFIRMED) {
            throw new IllegalStateException("Refund can only be issued for confirmed payments");
        }
        this.state = PaymentState.REFUND_REQUESTED;
        return RefundIssuedEvent.from(this);
    }

    public RefundSucceededEvent markRefundSucceeded() {
        this.state = PaymentState.REFUND_CONFIRMED;
        return RefundSucceededEvent.from(this);
    }

    public RefundFailedEvent markRefundFailed(String reason) {
        this.state = PaymentState.REFUND_FAILED;
        return RefundFailedEvent.from(this, reason);
    }

    @Override
    public PaymentId getId() {
        return paymentId;
    }

    public enum PaymentState {
        PAYMENT_PENDING,
        PAYMENT_CONFIRMED,
        PAYMENT_FAILED,
        REFUND_REQUESTED,
        REFUND_CONFIRMED,
        REFUND_FAILED,
    }
}
