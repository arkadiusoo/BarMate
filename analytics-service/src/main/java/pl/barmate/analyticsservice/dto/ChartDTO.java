package pl.barmate.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.barmate.analyticsservice.model.ChartType;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDTO {
    private Long id;
    private Long userId;
    private Date created;
    private String chartName;
    private ChartType chartType;
    private String chartData;
}
