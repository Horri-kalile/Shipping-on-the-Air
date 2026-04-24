package distributed_sota.api_gateway.infrastructure.proxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class DeliveryProxy extends HttpSyncBaseProxy {

    private final String baseUrl;

    public DeliveryProxy(RestTemplate restTemplate,
                         @Value("${services.delivery.base-url}") String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public String createDelivery(Object dto) {
        return post(baseUrl, dto, String.class);
    }

    public void startDelivery(String id) {
        post(baseUrl + "/" + id + "/start", null, Void.class);
    }

    public String getStatus(String id) {
        return get(baseUrl + "/" + id + "/status", String.class);
    }

    public String getRemainingTime(String id) {
        return get(baseUrl + "/" + id + "/remaining-time", String.class);
    }
}
