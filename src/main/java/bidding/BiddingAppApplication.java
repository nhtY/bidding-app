package bidding;

import bidding.model.Offer;
import bidding.model.Product;
import bidding.model.Product$;
import bidding.model.User;
import bidding.repository.OfferRepository;
import bidding.repository.ProductRepository;
import bidding.repository.UserRepository;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
@EnableRedisDocumentRepositories(basePackages = "bidding.*")
public class BiddingAppApplication {

	public BiddingAppApplication(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(BiddingAppApplication.class, args);
	}

	private PasswordEncoder passwordEncoder;

	// pre-load data to redis:
	@Bean
	CommandLineRunner loadTestData(UserRepository userRepo,
								   ProductRepository productRepo,
								   OfferRepository offerRepo) {
		return args -> {
			userRepo.deleteAll();
			productRepo.deleteAll();
			offerRepo.deleteAll();

			Product product1 = Product.of("Camera", "Old Camera", 25.0, "bit.ly/3AlicMg");
			Product product2 = Product.of("Old Gramophone", "Old gramophone on a dark background. music concept", 30.0, "bit.ly/3mXStGz");
			Product product3 = Product.of("Cassette Player", "The grandfather of the iPod.", 45.0, "bit.ly/3LlxGGb");

			User user1 = User.of("David", "Brown", "david", passwordEncoder.encode("david123"));
			User user2 = User.of("Bob", "Green", "bob", passwordEncoder.encode("bob123"));

			userRepo.saveAll(Arrays.asList(user1, user2));

			productRepo.saveAll(Arrays.asList(product1, product2, product3));
			log.warn("SAVED PRODUCTS: {}", productRepo.findAll());

			Product saved1 = productRepo.findByProductName("Camera");
			if(saved1 != null) {
				offerRepo.save(Offer.of(saved1.getId(), userRepo.findByUsername("bob").getId(), 50.0));
				offerRepo.save(Offer.of(saved1.getId(), userRepo.findByUsername("david").getId(), 75.0));
			}


			log.info("Saved Users: {}", userRepo.findAll());
			log.info("Saved User BOB: {}", userRepo.findByUsername("bob"));

			log.warn("PRODUCTS: {}", productRepo.findAll());

		};
	}

}
