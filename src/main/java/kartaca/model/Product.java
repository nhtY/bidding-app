package kartaca.model;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Document
public class Product {

    @Id
    @Indexed
    private String id;

    @Indexed @NonNull
    private String productName;

    @Indexed @NonNull
    private String description;

    @Indexed @NonNull
    private double price;

    @Indexed @NonNull
    private List<Offer> offers;

}
