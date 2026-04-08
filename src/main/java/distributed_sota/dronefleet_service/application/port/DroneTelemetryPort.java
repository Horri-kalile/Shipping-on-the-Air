package distributed_sota.dronefleet_service.application.port;

public interface DroneTelemetryPort {
    void simulateStep(int minutesSimulated);
}
