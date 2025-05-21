package pl.barmate.analyticsservice.dto;

import pl.barmate.analyticsservice.model.Chart;

public class ChartMapper {

    public static ChartDTO toDto(Chart chart) {
        if (chart == null) return null;

        return ChartDTO.builder()
                .id(chart.getId())
                .userId(chart.getUserId())
                .created(chart.getCreated())
                .chartName(chart.getChartName())
                .chartType(chart.getChartType())
                .chartData(chart.getChartData())
                .build();
    }

    public static Chart toEntity(ChartDTO dto) {
        if (dto == null) return null;

        return Chart.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .created(dto.getCreated())
                .chartName(dto.getChartName())
                .chartType(dto.getChartType())
                .chartData(dto.getChartData())
                .build();
    }
}