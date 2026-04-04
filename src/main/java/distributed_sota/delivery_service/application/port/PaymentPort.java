package distributed_sota.delivery_service.application.port;

import common.hexagonal.OutBoundPort;
import distributed_sota.delivery_service.application.exception.PaymentFailedException;
import distributed_sota.delivery_service.application.exception.RefundFailedException;
import distributed_sota.delivery_service.domain.model.DeliveryId;
import distributed_sota.delivery_service.domain.model.Price;

@OutBoundPort
public interface PaymentPort {
    void requestPayment(String userId, DeliveryId deliveryId, Price amount) throws PaymentFailedException;

    void requestRefund(String userId, DeliveryId deliveryId, Price amount) throws RefundFailedException;
}
