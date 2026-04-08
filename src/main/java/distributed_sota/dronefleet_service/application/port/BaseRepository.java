// file: src/main/java/distributed_sota/dronefleet_service/application/port/BaseRepositoryPort.java
package distributed_sota.dronefleet_service.application.port;

import distributed_sota.dronefleet_service.domain.model.Base;
import distributed_sota.dronefleet_service.domain.model.BaseId;
import distributed_sota.dronefleet_service.domain.model.Location;

import java.util.Optional;

public interface BaseRepository {
    boolean  isPresent(BaseId baseId);
    boolean isPresentByLoc(Location location);
    Optional<Base> findById(BaseId baseId);
    Optional<Base> findClosestBase(Location location);
    void save(Base base);
}
