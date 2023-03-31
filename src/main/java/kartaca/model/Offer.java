package kartaca.model;

import lombok.Data;

@Data
public class Offer {

    private String id;

    // the owner of the offer
    private String ownerId;

    private double amount;

}
