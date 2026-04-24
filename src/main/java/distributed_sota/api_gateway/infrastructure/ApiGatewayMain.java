package distributed_sota.api_gateway.infrastructure;

import distributed_sota.api_gateway.infrastructure.proxy.*;
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
    public DeliveryProxy deliveryProxy(RestTemplate restTemplate) {
        return new DeliveryProxy(restTemplate);
    }

    @Bean
    public UserProxy userProxy(RestTemplate restTemplate) {
        return new UserProxy(restTemplate);
    }

    @Bean
    public PaymentProxy paymentProxy(RestTemplate restTemplate) {
        return new PaymentProxy(restTemplate);
    }

    @Bean
    public DroneProxy droneProxy(RestTemplate restTemplate) {
        return new DroneProxy(restTemplate);
    }
}
