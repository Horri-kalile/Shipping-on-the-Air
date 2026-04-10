package distributed_sota.delivery_service.infrastructure.event;

import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import distributed_sota.delivery_service.application.port.DomainEventPublisher;
import distributed_sota.delivery_service.domain.event.DeliveryEvent;

public class DomainEventPublisherImpl implements DomainEventPublisher {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String topic;

    public DomainEventPublisherImpl(KafkaTemplate<String, String> kafka, String topic) {
        this.kafka = kafka;
        this.topic = topic;
    }

    @Override
    public void publishEvent(DeliveryEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            kafka.send(topic, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}
