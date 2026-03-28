package distributed_sota.delivery_service.domain.event;

import distributed_sota.delivery_service.domain.model.DeliveryId;

/**
 *
 * Domain event: delivery started
 *
 */
public record DeliveryStartedEvent (DeliveryId id, String userId) implements DeliveryEvent {}