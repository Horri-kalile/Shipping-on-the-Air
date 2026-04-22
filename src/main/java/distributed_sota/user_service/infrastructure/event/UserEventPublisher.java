package distributed_sota.user_service.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import distributed_sota.user_service.application.port.UserEventPublisherPort;
import distributed_sota.user_service.domain.event.UserDeletedEvent;
import distributed_sota.user_service.domain.event.UserEmailUpdatedEvent;
import distributed_sota.user_service.domain.event.UserRegisteredEvent;
import org.springframework.kafka.core.KafkaTemplate;

public class UserEventPublisher implements UserEventPublisherPort {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;
    private final String topic;

    public UserEventPublisher(KafkaTemplate<String, String> kafka,
                              ObjectMapper mapper,
                              String topic) {
        this.kafka = kafka;
        this.mapper = mapper;
        this.topic = topic;
    }

    @Override
    public void publish(Object event) {
        try {
            ObjectNode node = mapper.createObjectNode();

            node.put("type", event.getClass().getSimpleName());

            if (event instanceof UserRegisteredEvent e) {
                node.put("userId", e.userId());
                node.put("username", e.username());
                node.put("email", e.email());
            }

            else if (event instanceof UserEmailUpdatedEvent e) {
                node.put("userId", e.userId());
                node.put("oldEmail", e.oldEmail());
                node.put("newEmail", e.newEmail());
            }

            else if (event instanceof UserDeletedEvent e) {
                node.put("userId", e.userId());
            }

            String json = mapper.writeValueAsString(node);

            kafka.send(topic, json);
            kafka.flush();

        } catch (Exception e) {
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}
