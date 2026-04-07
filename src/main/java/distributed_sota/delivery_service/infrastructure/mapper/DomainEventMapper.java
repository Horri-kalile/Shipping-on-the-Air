package distributed_sota.delivery_service.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import distributed_sota.delivery_service.domain.event.DeliveryEvent;

/**
 * Mapper générique pour sérialiser et désérialiser les events
 * sans dépendre directement d'autres microservices.
 */

@Component
public class DomainEventMapper {

    private final ObjectMapper objectMapper;

    public DomainEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Object fromJson(String json) {
        try {
            ObjectNode node = (ObjectNode) objectMapper.readTree(json);
            String type = node.has("type") ? node.get("type").asText() : "";

            return switch (type) {
                case "DeliveryCreatedEvent",
                     "DeliveryCompletedEvent",
                     "DeliveryFailedEvent",
                     "DeliveryStartedEvent",
                     "EtaUpdatedEvent",
                     "DeliveryCanceledEvent"
                        -> objectMapper.treeToValue(node, DeliveryEvent.class);

                default -> throw new IllegalArgumentException("Unknown delivery event type: " + type);
            };

        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize delivery event", e);
        }
    }
}
