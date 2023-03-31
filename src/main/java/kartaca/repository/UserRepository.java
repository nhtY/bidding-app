package kartaca.repository;

import com.redis.om.spring.repository.RedisDocumentRepository;
import kartaca.model.User;

public interface UserRepository extends RedisDocumentRepository<User, String> {
}
