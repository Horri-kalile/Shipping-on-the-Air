package distributed_sota.user_service.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import common.ddd.Aggregate;

public class User implements Aggregate<UserId> {

    private final UserId userId;
    private Password password;
    private Email email;
    private final List<String> deliveryIds;

    public User(UserId userId, Password password, Email email) {
        this(userId, password, email, new ArrayList<>());
    }

    private User(UserId userId, Password password, Email email, List<String> deliveryIds) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.deliveryIds = new ArrayList<>(deliveryIds);
    }

    public static User rehydrate(UserId userId, Password password, Email email, List<String> deliveryIds) {
        return new User(userId, password, email, deliveryIds == null ? List.of() : deliveryIds);
    }

    @Override
    public UserId getId() {
        return userId;
    }

    public Password getPassword() {
        return password;
    }

    public void withPassword(Password newPassword) {
        this.password = newPassword;
    }

    public Email getEmail() {
        return email;
    }

    public void withEmail(Email newEmail) {
        this.email = newEmail;
    }

    public UserInfo getUserInfo() {
        return new UserInfo(userId.toString(), email.toString(), Instant.now().toEpochMilli());
    }

    public void addDeliveryId(String deliveryId) {
        this.deliveryIds.add(deliveryId);
    }

    public List<String> getDeliveryIds() {
        return List.copyOf(deliveryIds);
    }
}
