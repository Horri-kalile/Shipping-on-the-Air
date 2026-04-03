package distributed_sota.user_service.application.port;

public interface UserEventPublisherPort {
    void publish(Object event);
}
