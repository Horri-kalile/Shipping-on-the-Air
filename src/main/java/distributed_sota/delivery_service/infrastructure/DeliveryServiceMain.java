package distributed_sota.delivery_service.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import distributed_sota.delivery_service.application.port.DeliveryRepository;
import distributed_sota.delivery_service.application.port.DeliveryService;
import distributed_sota.delivery_service.application.port.DomainEventPublisher;
import distributed_sota.delivery_service.application.port.DroneFleetPort;
import distributed_sota.delivery_service.application.port.PaymentPort;
import distributed_sota.delivery_service.application.service.DeliveryServiceImpl;
import distributed_sota.delivery_service.domain.service.DeliveryCalculator;
import distributed_sota.delivery_service.infrastructure.adapter.DroneFleetAdapter;
import distributed_sota.delivery_service.infrastructure.adapter.PaymentAdapter;
import distributed_sota.delivery_service.infrastructure.event.DomainEventPublisherImpl;

@SpringBootApplication
public class DeliveryServiceMain {

    @Value("${services.payment.base-url}")
    private String paymentBaseUrl;

    @Value("${services.dronefleet.base-url}")
    private String droneFleetBaseUrl;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DeliveryServiceMain.class);
        app.setAdditionalProfiles("delivery");
        app.run(args);
    }

    
    // REST TEMPLATE
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    
    // DOMAIN SERVICE
    
    @Bean
    public DeliveryCalculator deliveryCalculator() {
        return new DeliveryCalculator();
    }

    
    // ADAPTERS : Payment + DroneFleet
    
    @Bean
    public PaymentPort paymentPort(RestTemplate rest) {
        return new PaymentAdapter(rest, paymentBaseUrl);
    }

    @Bean
    public DroneFleetPort droneFleetPort(RestTemplate rest) {
        return new DroneFleetAdapter(rest, droneFleetBaseUrl);
    }

    
    // DOMAIN EVENT PUBLISHER (Kafka)
    
    @Bean
    public DomainEventPublisher domainEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        return new DomainEventPublisherImpl(kafkaTemplate,"delivery-events");
    }

    
    // APPLICATION SERVICE
    
    @Bean
    public DeliveryService deliveryService(
            DeliveryRepository repository,
            DeliveryCalculator calculator,
            PaymentPort paymentPort,
            DroneFleetPort droneFleetPort,
            DomainEventPublisher domainEventPublisher
    ) {
        return new DeliveryServiceImpl(
                repository,
                calculator,
                paymentPort,
                droneFleetPort,
                domainEventPublisher
        );
    }
}
