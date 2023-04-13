package bidding.repository;

// import com.redis.om.spring.repository.RedisDocumentRepository;
import bidding.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {

}
