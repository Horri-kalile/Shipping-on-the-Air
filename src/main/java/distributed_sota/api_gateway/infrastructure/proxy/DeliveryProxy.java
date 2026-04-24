package distributed_sota.api_gateway.infrastructure.proxy;
import org.springframework.web.client.RestTemplate;

public class DeliveryProxy extends HttpSyncBaseProxy {

    private final String BASE_URL = "http://delivery-service:8080/deliveries";

    public DeliveryProxy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public String createDelivery(Object dto) {
        return post(BASE_URL, dto, String.class);
    }

    public void startDelivery(String id) {
        post(BASE_URL + "/" + id + "/start", null, Void.class);
    }

    public String getStatus(String id) {
        return get(BASE_URL + "/" + id + "/status", String.class);
    }

    public String getRemainingTime(String id) {
        return get(BASE_URL + "/" + id + "/remaining-time", String.class);
    }
}