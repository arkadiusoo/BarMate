package pl.barmate.analyticsservice.unit;

import org.junit.jupiter.api.Test;
import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.model.ChartType;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ChartHistoryDTOTest {

    @Test
    void shouldCreateChartHistoryDTOCorrectly() {

        Long id = 1L;
        Long userId = 100L;
        Date created = new Date();
        String chartName = "Test Chart";
        ChartType chartType = ChartType.TheMostPopularRecipies;


        ChartHistoryDTO dto = new ChartHistoryDTO(id, userId, created, chartName, chartType);


        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.created()).isEqualTo(created);
        assertThat(dto.chartName()).isEqualTo(chartName);
        assertThat(dto.chartType()).isEqualTo(chartType);
    }
}
