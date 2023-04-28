package bidding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter @Setter
@AllArgsConstructor
public class OfferMessage {
    private String userId;
    private  double offer;
}
