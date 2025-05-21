package pl.barmate.analyticsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.barmate.analyticsservice.model.Chart;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<Chart, Long> {
    List<Chart> findByUserId(Long userId);
    List<Chart> findByUserIdAndDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<Chart> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
