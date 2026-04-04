package distributed_sota.delivery_service.application.exception;
import distributed_sota.delivery_service.domain.model.DeliveryId;

public class DeliveryNotFoundException extends Exception {

    private final DeliveryId deliveryId;

    public DeliveryNotFoundException(DeliveryId deliveryId) {
        super("Delivery not found: " + deliveryId);
        this.deliveryId = deliveryId;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }
}
