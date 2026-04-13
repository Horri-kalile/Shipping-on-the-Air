package distributed_sota.delivery_service.infrastructure.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataDeliveryMongoRepository extends MongoRepository<DeliveryDocument, String> {

    List<DeliveryDocument> findByUserId(String userId);

    List<DeliveryDocument> findByDroneId(String droneId);

    Optional<DeliveryDocument> findTopByOrderByIdDesc();
}