package bidding.repository;


import bidding.model.Product;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.List;

public interface ProductRepository extends RedisDocumentRepository<Product, String> {
    Product findByProductName(String productName);

    List<Product> findByProductOwner(String username);
}
