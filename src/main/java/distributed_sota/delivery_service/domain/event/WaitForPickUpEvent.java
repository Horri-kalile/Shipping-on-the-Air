package distributed_sota.delivery_service.domain.event;

import distributed_sota.delivery_service.domain.model.DeliveryId;

public record WaitForPickUpEvent(DeliveryId id, String userId) implements DeliveryEvent {}
