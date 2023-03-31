package kartaca.repository;

import com.redis.om.spring.repository.RedisDocumentRepository;
import kartaca.model.Product;

public interface ProductRepository extends RedisDocumentRepository<Product, String> {

}
