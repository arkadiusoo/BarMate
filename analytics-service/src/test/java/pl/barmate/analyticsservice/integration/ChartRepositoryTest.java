package pl.barmate.analyticsservice.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.barmate.analyticsservice.model.Chart;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.repository.ChartRepository;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
public class ChartRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ChartRepository chartRepository;

    @BeforeEach
    void setUp() {
        chartRepository.deleteAll();

        Chart chart1 = Chart.builder()
                .userId(1L)
                .chartType(ChartType.TheMostPopularRecipies)
                .chartName("Chart 1")
                .chartData("[]")
                .created(new Date())
                .build();

        Chart chart2 = Chart.builder()
                .userId(1L)
                .chartType(ChartType.ConsuptionInTime)
                .chartName("Chart 2")
                .chartData("[]")
                .created(new Date())
                .build();

        Chart chart3 = Chart.builder()
                .userId(2L)
                .chartType(ChartType.TheMostPopularIngredients)
                .chartName("Chart 3")
                .chartData("[]")
                .created(new Date())
                .build();

        chartRepository.saveAll(List.of(chart1, chart2, chart3));
    }

    @Test
    void shouldFindAllChartsByUserId() {
        List<Chart> user1Charts = chartRepository.findAllByUserId(1L);
        List<Chart> user2Charts = chartRepository.findAllByUserId(2L);
        List<Chart> noUserCharts = chartRepository.findAllByUserId(999L);

        assertThat(user1Charts).hasSize(2);
        assertThat(user2Charts).hasSize(1);
        assertThat(noUserCharts).isEmpty();
    }
}
