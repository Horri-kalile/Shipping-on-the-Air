package distributed_sota.delivery_service.infrastructure.repository.mongo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import distributed_sota.delivery_service.application.port.DeliveryRepository;
import distributed_sota.delivery_service.domain.model.Delivery;
import distributed_sota.delivery_service.domain.model.DeliveryId;

@Repository
public class MongoDeliveryRepositoryAdapter implements DeliveryRepository {

    private final SpringDataDeliveryMongoRepository mongoRepository;
    private final AtomicInteger counter = new AtomicInteger(0);

    public MongoDeliveryRepositoryAdapter(SpringDataDeliveryMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void updateDelivery(Delivery delivery) {
        mongoRepository.save(DeliveryDocumentMapper.toDocument(delivery));
    }

    @Override
    public void saveDelivery(Delivery delivery) {
        mongoRepository.save(DeliveryDocumentMapper.toDocument(delivery));
    }

    @Override
    public void deleteDelivery(Delivery delivery) {
        mongoRepository.deleteById(delivery.getId().id());
    }

    @Override
    public Optional<Delivery> findById(DeliveryId id) {
        return mongoRepository.findById(id.id()).map(DeliveryDocumentMapper::toDomain);
    }

    @Override
    public List<Delivery> findByUserId(String userId) {
        return mongoRepository.findByUserId(userId)
                .stream()
                .map(DeliveryDocumentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Delivery> findByDroneId(String droneId) {
        return mongoRepository.findByDroneId(droneId)
                .stream()
                .map(DeliveryDocumentMapper::toDomain)
                .toList();
    }

    @Override
    public String nextId() {
        return "order-" + String.format("%04d", counter.incrementAndGet());
    }

    @Override
    public boolean existsById(DeliveryId id) {
        return mongoRepository.existsById(id.id());
    }
}
