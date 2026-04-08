package distributed_sota.dronefleet_service.application.port;

import distributed_sota.dronefleet_service.domain.model.BaseId;
import distributed_sota.dronefleet_service.domain.model.Drone;
import distributed_sota.dronefleet_service.domain.model.DroneId;

import java.util.List;
import java.util.Optional;

public interface DroneRepository {
    Optional<Drone> findById(DroneId id);
    void save(Drone drone);
    boolean isPresent(DroneId droneId);
    List<Drone> findAll();

    List<Drone> findAllActive();

    List<Drone> findAvailableByBase(BaseId baseId);
    void deleteById(DroneId droneId);
    int nextId();
}
