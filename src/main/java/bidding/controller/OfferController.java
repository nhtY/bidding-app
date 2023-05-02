package bidding.controller;

import bidding.dto.OfferMessage;
import bidding.model.Offer;
import bidding.model.Product;
import bidding.repository.OfferRepository;
import bidding.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
public class OfferController {

    private ProductRepository productRepo;
    private OfferRepository offerRepo;
    public OfferController(ProductRepository productRepo, OfferRepository offerRepo) {
        this.productRepo = productRepo;
        this.offerRepo = offerRepo;
    }

    @MessageMapping("/bid/{id}") // The messages sent to this path will be handled by this method. "/{id}/bid"
    @SendTo("/topic/bidding/{id}") // clients listening to this path will see the returned message (Offer object) "/topic/bidding/{id}"
    public Offer giveOffer(@DestinationVariable String id,
                           OfferMessage offer){

        log.info("Offer from socket: {}", offer);

        Product product = productRepo.findById(id).orElse(null);
        Offer savedOffer = null;
        String userId;
        double amount;
        if(product != null) {
            log.info("SOCKET-OFFER: product with id {} FOUND", id);
            // TODO use HtmlUtils.htmlEscape(offer.getUserId()); and test if works
            userId = offer.getUserId(); //HtmlUtils.htmlEscape(offer.getUserId());
            amount = offer.getOffer();
            log.info("The Offer INFO: " + userId + " " + amount);

            savedOffer = offerRepo.save(Offer.of(id, userId, amount));

            log.info("SOCKET-OFFER: New Offer is saved {}", savedOffer);
        }
        return savedOffer;
    }

}