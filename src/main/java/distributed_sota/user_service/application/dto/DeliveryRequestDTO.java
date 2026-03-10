package distributed_sota.user_service.application.dto;

import java.time.Instant;

public record DeliveryRequestDTO(
        String userId,
        double pickupLocationLat,
        double pickupLocationLon,
        double dropoffLocationLat,
        double dropoffLocationLon,
        double weight,
        Instant requestedTimeStart,
        Instant requestedTimeEnd
) { }

