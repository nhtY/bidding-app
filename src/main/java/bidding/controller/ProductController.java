package bidding.controller;

import bidding.model.Offer;
import bidding.model.Product;
import bidding.repository.OfferRepository;
import bidding.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:5500"}) // http://127.0.0.1:5500 is vsCode testing
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
