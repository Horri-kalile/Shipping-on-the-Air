package distributed_sota.delivery_service.domain.event;

import distributed_sota.delivery_service.domain.model.DeliveryId;

/**
 *
 * Domain event: delivery failed
 *
 */
public record DeliveryFailedEvent (DeliveryId id, String userId, String reason) implements DeliveryEvent {}