package bidding.controller;

import bidding.dto.OfferMessage;
import bidding.model.Offer;
import bidding.model.Product;
import bidding.model.Product$;
import bidding.repository.OfferRepository;
import bidding.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;


import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductRepository productRepo;
    private OfferRepository offerRepo;


    public ProductController(ProductRepository productRepo, OfferRepository offerRepo) {
        this.productRepo = productRepo;
        this.offerRepo = offerRepo;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Product> getAllProducts() {
        return  productRepo.findAll();
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable String id){

        return productRepo.findById(id).orElse(null);
    }


    @MessageMapping("/{id}/bid") // The messages sent to this path will be handled by this method.
    @SendTo("/topic/bidding/{id}") // clients listening to this path will see the returned message (Offer object)
    public Offer giveOffer(@PathVariable String id,
                           OfferMessage offer){
        Product product = productRepo.findById(id).orElse(null);
        Offer savedOffer = null;
        String userId;
        double amount;
        if(product != null) {

            userId = HtmlUtils.htmlEscape(offer.getUserId());
            amount = offer.getOffer();

            savedOffer = offerRepo.save(Offer.of(id, userId, amount));
        }
       return savedOffer;
    }

    @GetMapping("/{id}/offer-list")
    @ResponseStatus(HttpStatus.OK)
    public List<Offer> findOffers(@PathVariable String id){

        List<Offer> offers = offerRepo.readByProductId(id);
        log.info("OFFERS: {}", offers);
        return offers;
    }

    @GetMapping("/{id}/offer-last2")
    public List<Offer> findLastOffers(@PathVariable String id){

        Page<Offer> offers = offerRepo.readByProductId(id, PageRequest.of(0,2, Sort.by("offerDate").descending()));
        log.info("OFFERS: {}", offers.getContent());
        return offers.getContent();
    }
}
