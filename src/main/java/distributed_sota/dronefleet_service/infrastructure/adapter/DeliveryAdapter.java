package distributed_sota.dronefleet_service.infrastructure.adapter;

import common.hexagonal.Adapter;
import distributed_sota.dronefleet_service.application.port.DeliveryServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Adapter
@Component
public class DeliveryAdapter implements DeliveryServicePort {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private static final Logger log = LoggerFactory.getLogger(DeliveryAdapter.class);


    public DeliveryAdapter(RestTemplate restTemplate,
                           @Value("${services.delivery.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean deliveryExists(String deliveryId) {
        log.info("[DRONE][DELIVERY-PORT] checking delivery existence id={}", deliveryId);
        String url = baseUrl + "/" + deliveryId + "/exists";
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }

    @Override
    public void assignDroneToDelivery(String deliveryId, String droneId) {
        String url = baseUrl + "/" + deliveryId + "/assignDrone";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AssignDroneRequest request = new AssignDroneRequest(droneId);
        HttpEntity<AssignDroneRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    // -------------------------------------------------
    // DTO internes
    // -------------------------------------------------

    private record AssignDroneRequest(String droneId) {}
}
