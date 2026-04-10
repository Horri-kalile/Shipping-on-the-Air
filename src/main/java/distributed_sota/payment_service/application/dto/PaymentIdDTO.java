package distributed_sota.payment_service.application.dto;

import distributed_sota.payment_service.domain.model.PaymentId;

public record PaymentIdDTO(String paymentId) {
    public PaymentId toDomain() {
        return new PaymentId(paymentId);
    }
}
