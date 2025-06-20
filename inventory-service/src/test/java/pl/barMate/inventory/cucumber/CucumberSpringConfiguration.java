package pl.barMate.inventory.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import pl.barMate.inventory.InventoryServiceApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = InventoryServiceApplication.class)
public class CucumberSpringConfiguration {
    // Pusta klasa, ale potrzebna do integracji Spring Boot + Cucumber
}
