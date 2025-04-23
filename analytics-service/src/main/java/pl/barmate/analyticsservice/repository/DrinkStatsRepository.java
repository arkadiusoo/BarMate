package pl.barmate.analyticsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.barmate.analyticsservice.model.DrinkStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DrinkStatsRepository extends JpaRepository<DrinkStats, Long> {

    List<DrinkStats> findByUserId(Long userId);

    List<DrinkStats> findByRecipeId(Long recipeId);

    List<DrinkStats> findByUserIdAndPreparedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    List<DrinkStats> findByPreparedAtBetween(LocalDateTime start, LocalDateTime end);
}