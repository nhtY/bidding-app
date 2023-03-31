package kartaca.repository;

// import com.redis.om.spring.repository.RedisDocumentRepository;
import kartaca.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {

}
