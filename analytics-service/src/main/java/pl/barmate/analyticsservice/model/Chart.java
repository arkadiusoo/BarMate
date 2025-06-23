package pl.barmate.analyticsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "charts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Date created;

    @Column(nullable = false)
    private String chartName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChartType chartType;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String chartData;
}