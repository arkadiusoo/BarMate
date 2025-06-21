package pl.barmate.analyticsservice.dto;

import pl.barmate.analyticsservice.model.ChartType;

import java.util.Date;

public record ChartHistoryDTO (
    Long id,
    Long userId,
    Date created,
    String chartName,
    ChartType chartType
) {}