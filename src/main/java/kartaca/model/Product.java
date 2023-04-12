package kartaca.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@RedisHash("PRODUCTS")
public class Product {

    @Id @Indexed
    private String id;

    @NonNull @Indexed
    private String productName;

    @NonNull
    private String description;

    @NonNull
    private double price;

    @NonNull
    private List<Offer> offers;

    public void addOffer(Offer newOffer) {
        this.offers.add(newOffer);
    }

}
