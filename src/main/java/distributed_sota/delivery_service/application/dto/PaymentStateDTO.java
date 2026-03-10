package distributed_sota.delivery_service.application;

public enum PaymentStateDTO {
    PAYMENT_PENDING,
    PAYMENT_CONFIRMED,
    PAYMENT_FAILED,
    REFUND_REQUESTED,
    REFUND_CONFIRMED,
    REFUND_FAILED
}
