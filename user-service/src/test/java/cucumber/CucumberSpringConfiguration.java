package cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.barMate.UserServiceApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = UserServiceApplication.class)
public class CucumberSpringConfiguration {
}

