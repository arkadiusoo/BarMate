package pl.barmate.cocktails.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI cocktailOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cocktail API Microservice")
                        .description("REST API do wyszukiwania i filtrowania drinków z TheCocktailDB")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jakub Lasota")
                                .email("247717@edu.p.lodz.pl")
                                .url(""))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("TheCocktailDB API Docs")
                        .url("https://www.thecocktaildb.com/api.php"));
    }

    @Bean
    public GroupedOpenApi cocktailApiGroup() {
        return GroupedOpenApi.builder()
                .group("cocktails")
                .pathsToMatch("/api/cocktails/**")
                .build();
    }
}
