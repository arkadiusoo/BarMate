package pl.barmate.analyticsservice.behavioral;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.service.ChartService;

import static org.assertj.core.api.Assertions.assertThat;
@Component
public class ChartGenerationSteps {

    @Autowired
    private ChartService chartService;

    private Long userId;
    private byte[] generatedChart;

    @Given("a user with ID {int} exists")
    public void aUserWithIdExists(int id) {
        this.userId = (long) id;
        // zakładamy, że użytkownik istnieje – setup w bazie lub mock w testach
    }

    @When("the user requests a chart for the most popular recipes")
    public void theUserRequestsChartForMostPopularRecipes() {
        this.generatedChart = chartService.generateChart(ChartType.TheMostPopularRecipies, userId);
    }

    @Then("the chart is generated successfully")
    public void theChartIsGeneratedSuccessfully() {
        assertThat(generatedChart).isNotNull();
        assertThat(generatedChart.length).isGreaterThan(0);
    }
}
