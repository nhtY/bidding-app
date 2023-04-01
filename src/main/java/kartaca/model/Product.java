package kartaca.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@RedisHash("PRODUCTS")
public class Product {

    @Id
    private String id;

    @NonNull
    private String productName;

    @NonNull
    private String description;

    @NonNull
    private double price;

    @NonNull
    private List<Offer> offers;

}
