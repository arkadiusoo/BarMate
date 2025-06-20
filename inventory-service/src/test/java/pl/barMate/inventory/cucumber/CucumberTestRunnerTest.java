package pl.barMate.inventory.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "pl.barMate.inventory.cucumber"
)
public class CucumberTestRunnerTest {
}
