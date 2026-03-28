package distributed_sota.delivery_service.domain.event;

import distributed_sota.delivery_service.domain.model.DeliveryId;
import distributed_sota.delivery_service.domain.model.ETA;
import distributed_sota.delivery_service.domain.model.RemainingDuration;

import java.time.Instant;

public record EtaUpdatedEvent(
        DeliveryId deliveryId,
        String userId,
        ETA newEta,
        RemainingDuration remainingDuration,
        Instant occurredAt
) implements DeliveryEvent {

    public static EtaUpdatedEvent from(DeliveryId deliveryId, String userId, ETA newEta, RemainingDuration remainingDuration) {
        return new EtaUpdatedEvent(deliveryId, userId, newEta, remainingDuration, Instant.now());
    }
}
