package distributed_sota.user_service.infrastructure.repository.mongo;

import distributed_sota.user_service.domain.model.Email;
import distributed_sota.user_service.domain.model.Password;
import distributed_sota.user_service.domain.model.User;
import distributed_sota.user_service.domain.model.UserId;

public final class UserDocumentMapper {

    private UserDocumentMapper() {
    }

    public static UserDocument toDocument(User user) {
        return new UserDocument(
                user.getId().value(),
                user.getEmail().value(),
                user.getPassword().value(),
                user.getDeliveryIds()
        );
    }

    public static User toDomain(UserDocument document) {
        return User.rehydrate(
                UserId.is(document.getId()),
                Password.fromHash(document.getPasswordHash()),
                Email.is(document.getEmail()),
                document.getDeliveryIds()
        );
    }
}
