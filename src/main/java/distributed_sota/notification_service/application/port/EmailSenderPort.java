package distributed_sota.notification_service.application.port;

import common.hexagonal.OutBoundPort;
import distributed_sota.notification_service.domain.NotificationMessage;

@OutBoundPort
public interface EmailSenderPort {
    void sendEmail(NotificationMessage message);
}
