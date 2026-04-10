package distributed_sota.delivery_service.infrastructure.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.hexagonal.Adapter;
import distributed_sota.delivery_service.application.port.DeliveryRepository;
import distributed_sota.delivery_service.domain.model.Delivery;
import distributed_sota.delivery_service.domain.model.DeliveryId;


@Adapter
public class InMemoryDeliveryRepository implements DeliveryRepository {

    private static final Logger logger = Logger.getLogger("[DeliveryRepo]");
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<String, Delivery> deliveries = new ConcurrentHashMap<>();

    @Override
    public void updateDelivery(Delivery delivery) {
        deliveries.put(delivery.getId().id(), delivery);
        logger.log(Level.INFO, "Updated delivery {0}", delivery.getId().id());
    }

    @Override
    public void saveDelivery(Delivery delivery) {
        deliveries.put(delivery.getId().id(), delivery);
    }

    @Override
    public void deleteDelivery(Delivery delivery) {
        deliveries.remove(delivery.getId().id());
        logger.log(Level.INFO, "Deleted delivery {0}", delivery.getId().id());
    }

    @Override
    public Optional<Delivery> findById(DeliveryId id) {
        return Optional.ofNullable(deliveries.get(id.id()));
    }

    @Override
    public List<Delivery> findByUserId(String userId) {
        return deliveries.values()
                .stream()
                .filter(d -> d.getRequest().userId().equals(userId))
                .toList();
    }

    @Override
    public List<Delivery> findByDroneId(String droneId) {
        return deliveries.values()
                .stream()
                .filter(d -> d.getDroneId().isPresent() && d.getDroneId().equals(droneId))
                .toList();
    }

    @Override
    public String nextId() {
        return "order-" + String.format("%04d", counter.incrementAndGet());
    }

    @Override
    public boolean existsById(DeliveryId id) {
        return deliveries.containsKey(id.id());
    }
}
