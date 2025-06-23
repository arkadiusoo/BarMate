package pl.barmate.analyticsservice.service;

import pl.barmate.analyticsservice.dto.ChartHistoryDTO;
import pl.barmate.analyticsservice.model.ChartType;

import java.util.List;

public interface ChartService {
    byte[] generateChart(ChartType chartType, String userId, String username);
    byte[] regenerateChartFromHistory(Long chartId);

    List<ChartHistoryDTO> getUserChartHistory(String userName);
}
