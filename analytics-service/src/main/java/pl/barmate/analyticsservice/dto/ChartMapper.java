package pl.barmate.analyticsservice.dto;

import pl.barmate.analyticsservice.model.Chart;

public class ChartMapper {

    public static ChartHistoryDTO toHistoryDTO(Chart chart) {
        return new ChartHistoryDTO(
                chart.getId(),
                chart.getUserId(),
                chart.getCreated(),
                chart.getChartName(),
                chart.getChartType()
                );
    }

    public static Chart toEntity(ChartHistoryDTO dto) {
        if (dto == null) return null;

        return Chart.builder()
                .id(dto.id())
                .userId(dto.userId())
                .created(dto.created())
                .chartName(dto.chartName())
                .chartType(dto.chartType())
                .build();
    }
}