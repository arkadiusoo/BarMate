package pl.barMate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.barMate.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findUserProfileByUsername(String username);
}
