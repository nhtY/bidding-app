package bidding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class OfferMessage {
    private String userId;
    private  double offer;
}
