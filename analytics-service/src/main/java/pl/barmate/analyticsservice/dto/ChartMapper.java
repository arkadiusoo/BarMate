package pl.barmate.analyticsservice.dto;

import pl.barmate.analyticsservice.model.Chart;

public class ChartMapper {

    public static ChartHistoryDTO toHistoryDTO(Chart chart) {
        return ChartHistoryDTO.builder()
                .id(chart.getId())
                .userId(chart.getUserId())
                .created(chart.getCreated())
                .chartName(chart.getChartName())
                .chartType(chart.getChartType())
                .build();
    }

    public static Chart toEntity(ChartHistoryDTO dto) {
        if (dto == null) return null;

        return Chart.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .created(dto.getCreated())
                .chartName(dto.getChartName())
                .chartType(dto.getChartType())
                .build();
    }
}