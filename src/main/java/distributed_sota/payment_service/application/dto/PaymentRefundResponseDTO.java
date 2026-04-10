package distributed_sota.payment_service.application.dto;

public record PaymentRefundResponseDTO(
        String paymentId,
        String status
) {}
