package distributed_sota.user_service.domain.event;

public record UserRegisteredEvent(String userId, String email, String username, String type) {
    public UserRegisteredEvent(String userId, String email, String username) {
        this(userId, email, username, "UserRegisteredEvent");
    }
}
