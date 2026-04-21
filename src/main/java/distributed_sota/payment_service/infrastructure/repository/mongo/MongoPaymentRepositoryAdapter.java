package distributed_sota.payment_service.infrastructure.repository.mongo;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import common.hexagonal.Adapter;
import distributed_sota.payment_service.application.port.PaymentRepository;
import distributed_sota.payment_service.domain.model.Payment;
import distributed_sota.payment_service.domain.model.PaymentId;

@Adapter
@Component
@Primary
public class MongoPaymentRepositoryAdapter implements PaymentRepository {

    private final SpringDataPaymentMongoRepository mongoRepository;

    public MongoPaymentRepositoryAdapter(SpringDataPaymentMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void save(Payment payment) {
        mongoRepository.save(PaymentDocumentMapper.toDocument(payment));
    }

    @Override
    public Optional<Payment> findById(PaymentId paymentId) {
        return mongoRepository.findById(paymentId.toString())
                .map(PaymentDocumentMapper::toDomain);
    }

    @Override
    public void delete(PaymentId paymentId) {
        mongoRepository.deleteById(paymentId.toString());
    }

    @Override
    public boolean isPresent(PaymentId paymentId) {
        return mongoRepository.existsById(paymentId.toString());
    }
}
