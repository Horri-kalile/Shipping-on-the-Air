package distributed_sota.payment_service.application.port;

import common.hexagonal.OutBoundPort;
import distributed_sota.payment_service.domain.model.Amount;
import distributed_sota.payment_service.domain.model.PaymentId;

@OutBoundPort
public interface PaymentProcessPort {


    void processPayment(
            PaymentId paymentId,
            Amount amount,
            String userId,
            String deliveryId
    );

    void processRefund(
            PaymentId paymentId,
            Amount amount,
            String userId,
            String deliveryId
    );
}
