package bidding.repository;

import com.redis.om.spring.repository.RedisDocumentRepository;
import bidding.model.User;

public interface UserRepository extends RedisDocumentRepository<User, String> {

    User findByUsername(String username);
    Boolean existsByUsername(String username);

}
