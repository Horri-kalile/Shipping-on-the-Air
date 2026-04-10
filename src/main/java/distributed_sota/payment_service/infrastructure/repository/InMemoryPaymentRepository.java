package distributed_sota.payment_service.infrastructure.repository;

import distributed_sota.payment_service.application.port.PaymentRepository;
import distributed_sota.payment_service.domain.model.Payment;
import distributed_sota.payment_service.domain.model.PaymentId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<String, Payment> store = new ConcurrentHashMap<>();

    @Override
    public void save(Payment payment) {
        store.put(payment.getPaymentId().toString(), payment);
    }

    @Override
    public Optional<Payment> findById(PaymentId paymentId) {
        return Optional.ofNullable(store.get(paymentId.toString()));
    }

    @Override
    public void delete(PaymentId paymentId) {
        store.remove(paymentId.toString());
    }

    @Override
    public boolean isPresent(PaymentId paymentId) {
        return store.containsKey(paymentId.toString());
    }
}
