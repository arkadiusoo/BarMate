package pl.barmate.cocktails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient

@SpringBootApplication
public class DrinkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrinkServiceApplication.class, args);
    }

}
