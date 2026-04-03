package distributed_sota.user_service.application.repository;

import common.ddd.Repository;
import common.hexagonal.OutBoundPort;
import distributed_sota.user_service.application.exception.UserNotFoundException;
import distributed_sota.user_service.domain.model.*;

/**
 *
 * Interface of user repository
 *
 */
@OutBoundPort
public interface UserRepository extends Repository {

    void addUser(User user);

    void updateUser(User user);

    boolean isPresent(UserId userName);

    boolean isPresent(Email email);

    User findById(UserId userName) throws UserNotFoundException;

    User findByEmail(Email email) throws UserNotFoundException;

}