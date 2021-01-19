package api.bank.challenge;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ChallengeBankApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ChallengeBankApiApplication.class, args);
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/**"))
				.apis(RequestHandlerSelectors.basePackage("api.bank.challenge"))
				.build()
				.apiInfo(apiDetails());
	}
	
	private ApiInfo apiDetails() {
		return new ApiInfo(
		  "Challenge Bank API",
		  "Essential functions related to bank account management",
		  "1.0.0",
		  "This is free Licence version",
		  new springfox.documentation.service.Contact("André Luiz Cirino dos Santos", "http://github.com/javadevandre", "javadev.andre@gmail.com"),
		  "API License",
		  "http://github.com/javadevandre",
		  Collections.emptyList());
	}

}
