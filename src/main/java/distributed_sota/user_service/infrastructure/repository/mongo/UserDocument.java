package distributed_sota.user_service.infrastructure.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    private String email;
    private String passwordHash;
    private List<String> deliveryIds;

    public UserDocument() {
    }

    public UserDocument(String id, String email, String passwordHash, List<String> deliveryIds) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.deliveryIds = deliveryIds == null ? new ArrayList<>() : new ArrayList<>(deliveryIds);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<String> getDeliveryIds() {
        return deliveryIds == null ? new ArrayList<>() : new ArrayList<>(deliveryIds);
    }
}
