package pl.barmate.analyticsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.barmate.analyticsservice.model.Chart;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<Chart, Long> {
    List<Chart> findAllByUserId(Long userId);
}
