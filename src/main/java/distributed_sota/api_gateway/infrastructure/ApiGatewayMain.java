package distributed_sota.api_gateway.infrastructure;

import distributed_sota.api_gateway.infrastructure.proxy.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;
@SpringBootApplication
public class ApiGatewayMain {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiGatewayMain.class);
        app.setAdditionalProfiles("api-gateway");
        app.run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DeliveryProxy deliveryProxy(
            RestTemplate restTemplate,
            @Value("${services.delivery.base-url}") String deliveryBaseUrl
    ) {
        return new DeliveryProxy(restTemplate, deliveryBaseUrl);
    }

    @Bean
    public UserProxy userProxy(
            RestTemplate restTemplate,
            @Value("${services.user.base-url}") String userBaseUrl
    ) {
        return new UserProxy(restTemplate, userBaseUrl);
    }

    @Bean
    public PaymentProxy paymentProxy(
            RestTemplate restTemplate,
            @Value("${services.payment.base-url}") String paymentBaseUrl
    ) {
        return new PaymentProxy(restTemplate, paymentBaseUrl);
    }

    @Bean
    public DroneProxy droneProxy(
            RestTemplate restTemplate,
            @Value("${services.dronefleet.base-url}") String dronefleetBaseUrl
    ) {
        return new DroneProxy(restTemplate, dronefleetBaseUrl);
    }
}
