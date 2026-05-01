package distributed_sota.api_gateway.infrastructure.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class TrackingProxy extends HttpSyncBaseProxy {

    private final String baseUrl;

    public TrackingProxy(
            RestTemplate restTemplate,
            @Value("${services.tracking.base-url:${TRACKING_SERVICE_BASE_URL}}") String baseUrl
    ) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public String getTracking(String deliveryId) {
        return get(baseUrl + "/" + deliveryId, String.class);
    }
}
