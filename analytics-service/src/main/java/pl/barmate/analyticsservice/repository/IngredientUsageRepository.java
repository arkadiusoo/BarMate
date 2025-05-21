package pl.barmate.analyticsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngredientUsageRepository extends JpaRepository<IngredientUsage, Long> {

    List<IngredientUsage> findByUserId(Long userId);

    List<IngredientUsage> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<IngredientUsage> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<IngredientUsage> findByIngredientType(pl.barmate.analyticsservice.model.IngredientType type);

    List<IngredientUsage> findByUserIdAndIngredientType(Long userId, pl.barmate.analyticsservice.model.IngredientType type);
}