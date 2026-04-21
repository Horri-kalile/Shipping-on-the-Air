package distributed_sota.payment_service.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import distributed_sota.payment_service.application.service.PaymentServiceImpl;
import distributed_sota.payment_service.application.port.*;
import distributed_sota.payment_service.infrastructure.adapter.PaymentProcessorAdapter;
import distributed_sota.payment_service.infrastructure.event.PaymentEventPublisher;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * Payment Microservice Main entry point.
 */
@SpringBootApplication
public class PaymentServiceMain {

    @Value("${services.payment.base-url}")
    private String paymentBaseUrl;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PaymentServiceMain.class);
        app.setAdditionalProfiles("payment");
        app.run(args);
    }

    @PostConstruct
    public void testBean() {
        System.out.println(">>> PaymentServiceMain loaded <<<");
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // -------------------------------------------------------------------------
    // EVENT PUBLISHER
    // -------------------------------------------------------------------------
    @Bean
    public EventPublisherPort paymentEventPublisher(KafkaTemplate<String, String> kafka, ObjectMapper mapper) {
        return new PaymentEventPublisher(kafka,mapper, "payment-events");
    }

    // -------------------------------------------------------------------------
    // PAYMENT PROCESS ADAPTER
    // -------------------------------------------------------------------------
    @Bean
    public PaymentProcessPort paymentProcessPort(RestTemplate rest) {
        return new PaymentProcessorAdapter(rest,paymentBaseUrl);
    }

    // -------------------------------------------------------------------------
    // APPLICATION SERVICE
    // -------------------------------------------------------------------------
    @Bean
    public PaymentService paymentService(PaymentRepository repository,
                                         EventPublisherPort eventPublisher,
                                         PaymentProcessPort paymentProcess) {
        return new PaymentServiceImpl(repository, eventPublisher, paymentProcess);
    }
}
