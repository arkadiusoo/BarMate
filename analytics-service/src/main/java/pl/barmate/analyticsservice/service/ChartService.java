package pl.barmate.analyticsservice.service;

import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.model.ChartType;

import java.util.List;

public interface ChartService {
    byte[] generateChart(ChartType chartType, Long userId);
    byte[] regenerateChartFromHistory(Long chartId);

    List<ChartHistoryDTO> getUserChartHistory(Long userId);
}
