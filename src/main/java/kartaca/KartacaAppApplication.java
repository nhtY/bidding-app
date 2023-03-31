package kartaca;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import kartaca.model.User;
import kartaca.repository.ProductRepository;
import kartaca.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableRedisDocumentRepositories(basePackages = "kartaca.*")
@EnableSwagger2
@SpringBootApplication
public class KartacaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(KartacaAppApplication.class, args);
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
