package pl.barmate.analyticsservice.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.model.Chart;
import pl.barmate.analyticsservice.model.ChartType;
import pl.barmate.analyticsservice.repository.ChartRepository;
import pl.barmate.analyticsservice.service.ChartServiceImpl;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ChartServiceImplTestInt {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ChartServiceImpl chartService;

    @Autowired
    private ChartRepository chartRepository;

    @Test
    void shouldReturnChartHistoryForExistingUser() {

        Chart chart = Chart.builder()
                .userId(42L)
                .created(new Date())
                .chartType(ChartType.TheMostPopularIngredients)
                .chartName("Testowy wykres")
                .chartData("[]")
                .build();
        chartRepository.save(chart);


        List<ChartHistoryDTO> result = chartService.getUserChartHistory(42L);


        assertThat(result).isNotEmpty();
        assertThat(result.get(0).userId()).isEqualTo(42L);
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoCharts() {

        assertThrows(IllegalArgumentException.class,
                () -> chartService.getUserChartHistory(999L));
    }
}
