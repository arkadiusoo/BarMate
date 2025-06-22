package pl.barmate.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("UserRoute", r -> r.path("/user-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://user-service"))
                .route("ShoppingService", r-> r.path("/shopping_service/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://shopping_service"))
                .route("inventory_service", r-> r.path("/inventory_service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://inventory_service"))
                .route("drink-service", r-> r.path("/drink-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://drink-service"))
                .route("analytics-service", r-> r.path("/analytics-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://analytics-service"))
                .build();
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
