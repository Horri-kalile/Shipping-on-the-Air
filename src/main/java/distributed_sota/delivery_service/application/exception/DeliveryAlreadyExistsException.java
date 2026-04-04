package distributed_sota.delivery_service.application.exception;

import distributed_sota.delivery_service.domain.model.DeliveryId;

public class DeliveryAlreadyExistsException extends Exception {

    public DeliveryAlreadyExistsException(DeliveryId id) {
        super("Delivery already exists with id: " + id);
    }
}
