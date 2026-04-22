package distributed_sota.notification_service.application.port;

import common.hexagonal.InBoundPort;
import distributed_sota.notification_service.domain.NotificationMessage;

@InBoundPort
public interface NotificationService {
    void notify(NotificationMessage message);
}
