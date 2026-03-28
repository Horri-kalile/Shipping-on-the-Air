package distributed_sota.delivery_service.domain.event;

import distributed_sota.delivery_service.domain.model.DeliveryId;

import java.time.Instant;

/**
 * Drone requested
 */
public class DroneRequestedEvent implements DeliveryEvent {

    private final DeliveryId deliveryId;
    private final Instant occurredAt;

    public DroneRequestedEvent(DeliveryId deliveryId) {
        if (deliveryId == null) throw new IllegalArgumentException("DeliveryId cannot be null");
        this.deliveryId = deliveryId;
        this.occurredAt = Instant.now();
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
