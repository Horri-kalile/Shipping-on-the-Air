package distributed_sota.dronefleet_service.infrastructure.repository.mongo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import common.hexagonal.Adapter;
import distributed_sota.dronefleet_service.application.port.DroneRepository;
import distributed_sota.dronefleet_service.domain.model.BaseId;
import distributed_sota.dronefleet_service.domain.model.Drone;
import distributed_sota.dronefleet_service.domain.model.DroneId;
import distributed_sota.dronefleet_service.domain.model.DroneState;

@Adapter
@Component
@Primary
public class MongoDroneRepositoryAdapter implements DroneRepository {

    private final SpringDataDroneMongoRepository mongoRepository;
    private final AtomicInteger counter = new AtomicInteger(0);

    public MongoDroneRepositoryAdapter(SpringDataDroneMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<Drone> findById(DroneId id) {
        return mongoRepository.findById(id.id())
                .map(DroneDocumentMapper::toDomain);
    }

    @Override
    public void save(Drone drone) {
        mongoRepository.save(DroneDocumentMapper.toDocument(drone));
    }

    @Override
    public boolean isPresent(DroneId droneId) {
        return mongoRepository.existsById(droneId.id());
    }

    @Override
    public List<Drone> findAll() {
        return mongoRepository.findAll().stream()
                .map(DroneDocumentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Drone> findAllActive() {
        return mongoRepository.findByStateIn(List.of(DroneState.BUSY, DroneState.RETURNING)).stream()
                .map(DroneDocumentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Drone> findAvailableByBase(BaseId baseId) {
        return mongoRepository.findByBaseIdAndState(baseId.id(), DroneState.AVAILABLE).stream()
                .map(DroneDocumentMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(DroneId droneId) {
        mongoRepository.deleteById(droneId.id());
    }

    @Override
    public int nextId() {
        return counter.incrementAndGet();
    }
}
