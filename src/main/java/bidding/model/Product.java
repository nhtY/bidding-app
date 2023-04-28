package bidding.model;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Document
public class Product implements Serializable {

    @Id
    @Indexed
    private String id;

    @NonNull @Indexed
    private String productName;


    @NonNull
    private String description;

    @NonNull @Indexed
    private double basePrice;

    @Indexed
    private double soldPrice;

    @NonNull
    private String imgUrl;

    @Indexed
    private Date createdAt = new Date();

    @Indexed
    private boolean isSold = false;

}
