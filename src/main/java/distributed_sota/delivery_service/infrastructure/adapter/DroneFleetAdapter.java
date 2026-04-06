package distributed_sota.delivery_service.infrastructure.adapter;


import distributed_sota.delivery_service.application.dto.DroneAssignmentRequestDTO;
import distributed_sota.delivery_service.application.port.DroneFleetPort;
import distributed_sota.delivery_service.domain.model.Delivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DroneFleetAdapter implements DroneFleetPort {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private static final Logger log = LoggerFactory.getLogger(DroneFleetAdapter.class);

    public DroneFleetAdapter(RestTemplate restTemplate,
                             @Value("${services.dronefleet.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public String requestDrone(Delivery delivery) {
        String url = baseUrl + "/assign";
        log.info("[DELIVERY][DRONE] requesting drone for deliveryId={}", delivery.getId().id());

        DroneAssignmentRequestDTO body = new DroneAssignmentRequestDTO(
                delivery.getId().id(),
                delivery.getRequest().pickupLocation().latitude(),
                delivery.getRequest().pickupLocation().longitude(),
                delivery.getRequest().dropoffLocation().latitude(),
                delivery.getRequest().dropoffLocation().longitude(),
                delivery.getRequest().weight().value(),
                delivery.getRequest().userId()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DroneAssignmentRequestDTO> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
            log.info("[DELIVERY][DRONE] drone request successfully sent");
        } catch (Exception e) {
            log.error("[DELIVERY][DRONE] failed to send drone request", e);
            // IMPORTANT : on ne throw PAS
        }

        // Aucun droneId ici : il arrivera par event
        return null;
    }

}
