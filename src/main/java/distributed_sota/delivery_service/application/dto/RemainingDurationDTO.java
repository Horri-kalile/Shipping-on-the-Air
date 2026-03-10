package distributed_sota.delivery_service.application.dto;

public record RemainingDurationDTO(
        String deliveryId,
        long remainingMinutes
) { }
