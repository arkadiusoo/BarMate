package pl.barMate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.barMate.model.DrinkHistory;

import java.util.List;

public interface DrinkHistoryRepository extends JpaRepository<DrinkHistory, Long> {
    List<DrinkHistory> findByUserKeycloakId(String keycloakId);
}
