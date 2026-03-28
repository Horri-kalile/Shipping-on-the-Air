package distributed_sota.delivery_service.domain.event;

import distributed_sota.delivery_service.domain.model.DeliveryId;

/**
 *
 * Domain event: delivery canceled
 *
 */
public record DeliveryCanceledEvent (DeliveryId id, String userId, boolean refundNeeded)
        implements DeliveryEvent {}