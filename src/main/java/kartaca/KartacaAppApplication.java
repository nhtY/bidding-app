package kartaca;

import kartaca.model.Offer;
import kartaca.model.Product;
import kartaca.model.User;
import kartaca.repository.ProductRepository;
import kartaca.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;

//@EnableRedisDocumentRepositories(basePackages = "kartaca.*")
//@EnableSwagger2
@Slf4j
@SpringBootApplication
public class KartacaAppApplication {

	public KartacaAppApplication(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(KartacaAppApplication.class, args);
	}

	private PasswordEncoder passwordEncoder;

	// pre-load data to redis:
	@Bean
	CommandLineRunner loadTestData(UserRepository userRepo,
								   ProductRepository productRepo) {
		return args -> {
			userRepo.deleteAll();
			productRepo.deleteAll();

			Product product1 = Product.of("Product Name-1", "Description-1", 25.0, new ArrayList<>());
			Product product2 = Product.of("Product Name-2", "Description-2", 30.0, new ArrayList<>());
			Product product3 = Product.of("Product Name-3", "Description-3", 45.0, new ArrayList<>());



			User user1 = User.of("David", "Brown", "david", passwordEncoder.encode("david123"));
			User user2 = User.of("Bob", "Green", "bob", passwordEncoder.encode("bob123"));





			userRepo.saveAll(Arrays.asList(user1, user2));

			product1.addOffer(Offer.of(userRepo.findByUsername("bob").getId(), 35.0));

			productRepo.saveAll(Arrays.asList(product1, product2, product3));

			log.info("Saved Users: {}", userRepo.findAll());
			log.info("Saved User BOB: {}", userRepo.findByUsername("bob"));

		};
	}

	// add Swagger Docker Bean to the Spring App class
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}
