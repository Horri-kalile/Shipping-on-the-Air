package distributed_sota.payment_service.infrastructure.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataPaymentMongoRepository extends MongoRepository<PaymentDocument, String> {
}
