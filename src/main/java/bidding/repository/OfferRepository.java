package bidding.repository;

import bidding.model.Offer;
import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferRepository extends RedisDocumentRepository<Offer,String> {

    Page<Offer> readByProductId(String productId, Pageable pageable);

    List<Offer> readByProductId(String productId);
}
