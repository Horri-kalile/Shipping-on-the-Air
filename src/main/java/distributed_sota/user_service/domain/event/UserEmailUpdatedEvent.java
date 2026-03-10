package distributed_sota.user_service.domain.event;

public record UserEmailUpdatedEvent(String userId, String oldEmail, String newEmail, String type) {
    public UserEmailUpdatedEvent(String userId, String oldEmail, String newEmail) {
        this(userId, oldEmail, newEmail, "UserEmailUpdatedEvent");
    }
}
