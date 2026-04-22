package distributed_sota.user_service.infrastructure.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataUserMongoRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmail(String email);
    boolean existsByEmail(String email);
}
