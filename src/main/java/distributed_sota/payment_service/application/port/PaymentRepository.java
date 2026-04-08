package distributed_sota.payment_service.application.port;

import common.ddd.Repository;
import common.hexagonal.OutBoundPort;
import distributed_sota.payment_service.domain.model.Payment;
import distributed_sota.payment_service.domain.model.PaymentId;
import org.springframework.stereotype.Component;

import java.util.Optional;
@OutBoundPort
@Component
public interface PaymentRepository extends Repository {

    void save(Payment payment);

    Optional<Payment> findById(PaymentId paymentId);

    void delete(PaymentId paymentId);

    boolean isPresent(PaymentId paymentId);
}
