package distributed_sota.user_service.infrastructure;

import distributed_sota.payment_service.infrastructure.PaymentServiceMain;
import distributed_sota.user_service.application.port.DeliveryPort;
import distributed_sota.user_service.application.port.UserEventPublisherPort;
import distributed_sota.user_service.application.repository.UserRepository;
import distributed_sota.user_service.application.service.UserService;
import distributed_sota.user_service.application.service.UserServiceImpl;
import distributed_sota.user_service.infrastructure.adapter.DeliveryAdapter;
import distributed_sota.user_service.infrastructure.event.UserEventPublisher;
import distributed_sota.user_service.infrastructure.repository.InMemoryUserRepository;
import distributed_sota.user_service.infrastructure.controller.UserController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UserServiceMain {

    @Value("${services.delivery.base-url}")
    private String deliveryBaseUrl;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserServiceMain.class);
        app.setAdditionalProfiles("user");
        app.run(args);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // --- REPOSITORY ---
    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    // --- ADAPTER : DELIVERY → REST ---
    @Bean
    public DeliveryPort deliveryPort(RestTemplate rest) {
        return new DeliveryAdapter(rest, deliveryBaseUrl);
    }

    // --- ADAPTER : KAFKA EVENTS ---
    @Bean
    public UserEventPublisherPort eventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        return new UserEventPublisher(kafkaTemplate, "user-events");
    }

    // --- SERVICE (business logic) ---
    @Bean
    public UserService userService(
            UserRepository userRepository,
            DeliveryPort deliveryPort,
            UserEventPublisherPort eventPublisher
    ) {
        return new UserServiceImpl(userRepository, deliveryPort, eventPublisher);
    }

    // --- CONTROLLER (REST API) ---
    @Bean
    public UserController userController(UserService userService) {
        return new UserController(userService);
    }
}
