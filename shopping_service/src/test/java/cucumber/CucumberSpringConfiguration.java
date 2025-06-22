package cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.barMate.ShoppingServiceApplication;
import pl.barMate.controller.ShoppingListController;

@CucumberContextConfiguration
@SpringBootTest(classes = ShoppingServiceApplication.class)
public class CucumberSpringConfiguration {
}
