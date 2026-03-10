package distributed_sota.delivery_service.application.dto;

public record PaymentRequestDTO(
        String userId,
        String deliveryId,
        double amount,
        String currency
) { }
