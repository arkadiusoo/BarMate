package pl.barMate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import pl.barMate.model.DrinkHistory;
import pl.barMate.model.UserProfile;

import java.util.List;

@Repository
public interface DrinkHistoryRepository extends JpaRepository<DrinkHistory, Long> {
    List<DrinkHistory> findByUser_Username(String userUsername);
}
