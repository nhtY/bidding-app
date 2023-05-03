package bidding.model;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Document
public class Product implements Serializable {

    @Id
    @Indexed
    private String id;

    @Indexed @NonNull
    private String productOwner; // username of the owner

    @NonNull @Indexed
    private String productName;


    @NonNull
    private String description;

    @NonNull @Indexed
    private Double basePrice;

    @Indexed
    private Double soldPrice;

    @NonNull
    private String imgUrl;

    @Indexed
    private Date createdAt = new Date();

    @Indexed
    private Boolean isSold = false;

}
