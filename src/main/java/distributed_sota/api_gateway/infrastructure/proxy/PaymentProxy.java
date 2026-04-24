package distributed_sota.api_gateway.infrastructure.proxy;

import org.gradle.api.component.Component;
import org.springframework.web.client.RestTemplate;

public class PaymentProxy extends HttpSyncBaseProxy {

    private final String BASE_URL = "http://payment-service:8083/payments";

    public PaymentProxy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public void startPayment(String id) {
        post(BASE_URL + "/" + id + "/start", null, Void.class);
    }

    public void cancelPayment(String id) {
        post(BASE_URL + "/" + id + "/cancel", null, Void.class);
    }
}