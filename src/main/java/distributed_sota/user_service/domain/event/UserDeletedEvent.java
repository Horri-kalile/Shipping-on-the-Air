package distributed_sota.user_service.domain.event;

import java.time.Instant;

public record UserDeletedEvent(String userId, String type, Instant occurredAt) {
    public UserDeletedEvent(String userId) {
        this(userId, "UserDeletedEvent", Instant.now());
    }
}
