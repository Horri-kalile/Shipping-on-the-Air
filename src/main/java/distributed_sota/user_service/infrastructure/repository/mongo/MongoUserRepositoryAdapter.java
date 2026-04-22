package distributed_sota.user_service.infrastructure.repository.mongo;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import common.hexagonal.Adapter;
import distributed_sota.user_service.application.exception.UserNotFoundException;
import distributed_sota.user_service.application.repository.UserRepository;
import distributed_sota.user_service.domain.model.Email;
import distributed_sota.user_service.domain.model.User;
import distributed_sota.user_service.domain.model.UserId;

@Adapter
@Component
@Primary
public class MongoUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserMongoRepository mongoRepository;

    public MongoUserRepositoryAdapter(SpringDataUserMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void addUser(User user) {
        mongoRepository.save(UserDocumentMapper.toDocument(user));
    }

    @Override
    public void updateUser(User user) {
        mongoRepository.save(UserDocumentMapper.toDocument(user));
    }

    @Override
    public boolean isPresent(UserId userName) {
        return mongoRepository.existsById(userName.value());
    }

    @Override
    public boolean isPresent(Email email) {
        return mongoRepository.existsByEmail(email.value());
    }

    @Override
    public User findById(UserId userName) throws UserNotFoundException {
        return mongoRepository.findById(userName.value())
                .map(UserDocumentMapper::toDomain)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    @Override
    public User findByEmail(Email email) throws UserNotFoundException {
        return mongoRepository.findByEmail(email.value())
                .map(UserDocumentMapper::toDomain)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
