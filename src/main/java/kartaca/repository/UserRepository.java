package kartaca.repository;

// import com.redis.om.spring.repository.RedisDocumentRepository;
import kartaca.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
