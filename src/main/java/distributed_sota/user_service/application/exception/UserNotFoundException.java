package distributed_sota.user_service.application.exception;

import distributed_sota.user_service.domain.model.Email;
import distributed_sota.user_service.domain.model.UserId;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(UserId userName) {
    }

    public UserNotFoundException(Email email) {
    }
}