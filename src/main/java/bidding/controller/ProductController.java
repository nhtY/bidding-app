package bidding.controller;

import bidding.model.Offer;
import bidding.model.Product;
import bidding.repository.OfferRepository;
import bidding.repository.ProductRepository;
import bidding.utils.FormHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
//@CrossOrigin(origins = {"http://localhost:8080", }) // http://127.0.0.1:5500 is vsCode testing
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

    @GetMapping("/list-my-products")
    public List<Product> listByOwner(@RequestParam String username) {
        return productRepo.findByProductOwner(username);
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

    @PostMapping("/add-product")
    public ResponseEntity<Object> addProduct(@RequestBody  Product product, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.warn("FORM VALIDATION ERROR...register");
            return FormHandle.handleFormValidationError(bindingResult, HttpStatus.BAD_REQUEST);
        }

        Product savedProduct = productRepo.save(product);
        log.info("Saved Product = {}", savedProduct);

        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteproduct(@PathVariable("productId") String productId) {
        try {
            productRepo.deleteById(productId);
            log.info("Product with id={}, DELETED", productId);
        } catch (EmptyResultDataAccessException e) {}
    }

    @PatchMapping(path="/{productId}", consumes="application/json")
    public Product patchproduct(@PathVariable("productId") String productId,
                                @RequestBody Product patch) {
        Product product = productRepo.findById(productId).get();
        log.info("Before update= {}", product);

        if (patch.getProductName() != null) {
            product.setProductName(patch.getProductName());
        }
        if (patch.getDescription() != null) {
            product.setDescription(patch.getDescription());
        }
        if (patch.getImgUrl() != null) {
            product.setImgUrl(patch.getImgUrl());
        }
        if (patch.getBasePrice() != null) {
            product.setBasePrice(patch.getBasePrice());
        }
        log.info("After update= {}", product);
        return productRepo.save(product);
    }
}
