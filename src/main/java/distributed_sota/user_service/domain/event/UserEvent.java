package distributed_sota.user_service.domain.event;

import java.time.Instant;

public interface UserEvent { String type(); Instant occurredAt();
}
