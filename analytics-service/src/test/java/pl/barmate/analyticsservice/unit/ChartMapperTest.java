    package pl.barmate.analyticsservice.unit;

    import org.junit.jupiter.api.Test;
    import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
    import pl.barmate.analyticsservice.dto.ChartMapper;
    import pl.barmate.analyticsservice.model.Chart;
    import pl.barmate.analyticsservice.model.ChartType;

    import java.util.Date;

    import static org.assertj.core.api.Assertions.assertThat;

    public class ChartMapperTest {

        @Test
        void shouldMapChartToDTOCorrectly() {

            Chart chart = Chart.builder()
                    .id(1L)
                    .userId(42L)
                    .created(new Date())
                    .chartName("Example Chart")
                    .chartType(ChartType.TheMostPopularRecipies)
                    .build();


            ChartHistoryDTO dto = ChartMapper.toHistoryDTO(chart);


            assertThat(dto).isNotNull();
            assertThat(dto.id()).isEqualTo(chart.getId());
            assertThat(dto.userId()).isEqualTo(chart.getUserId());
            assertThat(dto.created()).isEqualTo(chart.getCreated());
            assertThat(dto.chartName()).isEqualTo(chart.getChartName());
            assertThat(dto.chartType()).isEqualTo(chart.getChartType());
        }

        @Test
        void shouldMapDTOToChartCorrectly() {

            Date created = new Date();
            ChartHistoryDTO dto = new ChartHistoryDTO(
                    2L,
                    77L,
                    created,
                    "History Chart",
                    ChartType.TheMostPopularRecipies
            );


            Chart chart = ChartMapper.toEntity(dto);


            assertThat(chart).isNotNull();
            assertThat(chart.getId()).isEqualTo(dto.id());
            assertThat(chart.getUserId()).isEqualTo(dto.userId());
            assertThat(chart.getCreated()).isEqualTo(dto.created());
            assertThat(chart.getChartName()).isEqualTo(dto.chartName());
            assertThat(chart.getChartType()).isEqualTo(dto.chartType());
        }

        @Test
        void shouldMapNullToNull() {
            assertThat(ChartMapper.toEntity(null)).isNull();
        }
    }
