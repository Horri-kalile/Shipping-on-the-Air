package distributed_sota.user_service.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import distributed_sota.user_service.application.port.UserEventPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;

public class UserEventPublisher implements UserEventPublisherPort {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String topic;

    public UserEventPublisher(KafkaTemplate<String, String> kafka, String topic) {
        this.kafka = kafka;
        this.topic = topic;
    }

    @Override
    public void publish(Object event) {
        try {
            // serialize and send
            String json = mapper.writeValueAsString(event);
            kafka.send(topic, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish user event", e);
        }
    }
}
