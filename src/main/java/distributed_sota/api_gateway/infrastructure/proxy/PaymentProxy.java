package distributed_sota.api_gateway.infrastructure.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class PaymentProxy extends HttpSyncBaseProxy {

    private final String baseUrl;

    public PaymentProxy(RestTemplate restTemplate,
                        @Value("${services.payment.base-url}") String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public void startPayment(String id) {
        post(baseUrl + "/" + id + "/start", null, Void.class);
    }

    public void cancelPayment(String id) {
        post(baseUrl + "/" + id + "/cancel", null, Void.class);
    }
}
