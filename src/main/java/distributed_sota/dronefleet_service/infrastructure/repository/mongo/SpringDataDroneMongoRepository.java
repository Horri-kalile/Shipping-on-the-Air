package distributed_sota.dronefleet_service.infrastructure.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import distributed_sota.dronefleet_service.domain.model.DroneState;

public interface SpringDataDroneMongoRepository extends MongoRepository<DroneDocument, String> {
    List<DroneDocument> findByStateIn(List<DroneState> states);
    List<DroneDocument> findByBaseIdAndState(String baseId, DroneState state);
}
