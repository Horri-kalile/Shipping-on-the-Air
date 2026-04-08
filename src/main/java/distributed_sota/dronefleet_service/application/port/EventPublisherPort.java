package distributed_sota.dronefleet_service.application.port;

import common.hexagonal.OutBoundPort;
import distributed_sota.dronefleet_service.domain.event.DroneEvent;

@OutBoundPort
public interface EventPublisherPort {
    void publish(DroneEvent event);
}
