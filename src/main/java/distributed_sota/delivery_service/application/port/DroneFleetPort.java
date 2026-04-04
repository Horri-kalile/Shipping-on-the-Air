package distributed_sota.delivery_service.application.port;

import common.hexagonal.OutBoundPort;
import distributed_sota.delivery_service.application.exception.DroneAssignmentFailedException;
import distributed_sota.delivery_service.domain.model.Delivery;

@OutBoundPort
public interface DroneFleetPort {

    String requestDrone(Delivery delivery) throws DroneAssignmentFailedException;
}
