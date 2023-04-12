package kartaca.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@RedisHash("OFFERS")
public class Offer {

    @Id @Indexed
    private String id;

    // the owner of the offer
    @NonNull
    private String ownerId;

    @NonNull
    private double amount;

}
