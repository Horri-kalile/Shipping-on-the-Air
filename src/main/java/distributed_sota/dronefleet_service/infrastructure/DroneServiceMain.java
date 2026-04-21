package distributed_sota.dronefleet_service.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import distributed_sota.dronefleet_service.application.port.BaseRepository;
import distributed_sota.dronefleet_service.application.port.DeliveryServicePort;
import distributed_sota.dronefleet_service.application.port.DroneRepository;
import distributed_sota.dronefleet_service.application.port.DroneService;
import distributed_sota.dronefleet_service.application.port.DroneTelemetryPort;
import distributed_sota.dronefleet_service.application.port.EventPublisherPort;
import distributed_sota.dronefleet_service.application.service.DroneServiceImpl;
import distributed_sota.dronefleet_service.domain.service.DroneCalculator;
import distributed_sota.dronefleet_service.infrastructure.adapter.DeliveryAdapter;
import distributed_sota.dronefleet_service.infrastructure.adapter.DroneTelemetryAdapter;
import distributed_sota.dronefleet_service.infrastructure.event.DroneEventPublisher;

@SpringBootApplication
@EnableScheduling
public class DroneServiceMain {

    @Value("${services.delivery.base-url}")
    private String deliveryBaseUrl;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DroneServiceMain.class);
        app.setAdditionalProfiles("dronefleet");
        app.run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // -------------------------------------------------------------------------
    // ADAPTERS (REST → DeliveryService)
    // -------------------------------------------------------------------------

    @Bean
    public DeliveryServicePort deliveryServicePort(RestTemplate rest) {
        return new DeliveryAdapter(rest,deliveryBaseUrl);
    }

    // -------------------------------------------------------------------------
    // EVENT PUBLISHER (Kafka → "drone-events")
    // -------------------------------------------------------------------------

    @Bean
    public EventPublisherPort eventPublisherPort(KafkaTemplate<String, String> kafka, ObjectMapper mapper) {
        return new DroneEventPublisher(kafka,mapper,"drone-events");
    }

    // -------------------------------------------------------------------------
    // DOMAIN SERVICE
    // -------------------------------------------------------------------------

    @Bean
    public DroneCalculator droneCalculator() {
        double batteryPercentPerKm = 1.2;
        return new DroneCalculator(batteryPercentPerKm);
    }

    // -------------------------------------------------------------------------
    // TELEMETRY SIMULATION
    // -------------------------------------------------------------------------

    @Bean
    public DroneTelemetryPort droneTelemetryPort(
            DroneRepository droneRepository,
            DroneService service
    ) {
        return new DroneTelemetryAdapter(droneRepository, service);
    }

    // -------------------------------------------------------------------------
    // APPLICATION SERVICE (DRONE SERVICE)
    // -------------------------------------------------------------------------

    @Bean
    public DroneService droneService(
            DroneRepository droneRepository,
            BaseRepository baseRepository,
            DeliveryServicePort deliveryServicePort,
            EventPublisherPort eventPublisherPort,
            DroneCalculator droneCalculator
    ) {
        double maintenanceThreshold = 200.0;

        return new DroneServiceImpl(
                droneRepository,
                baseRepository,
                deliveryServicePort,
                eventPublisherPort,
                droneCalculator,
                maintenanceThreshold
        );
    }
}
