package pl.barmate.analyticsservice.service;

import pl.barmate.analyticsservice.model.ChartType;

public interface ChartService {
    byte[] generateChart(ChartType chartType, Long userId);
    byte[] regenerateChartFromHistory(Long chartId);
}
