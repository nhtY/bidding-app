package bidding.model;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Document
public class Offer {

    @Id @Indexed
    private String id;

    @Indexed @NonNull
    private String productId;

    @Indexed
    private Date offerDate = new Date();

    // refers to the product to which this offer is given

    // the owner of the offer, it refers to a user
    @Indexed @NonNull
    private String givenById;

    @NonNull
    private double amount;

}
