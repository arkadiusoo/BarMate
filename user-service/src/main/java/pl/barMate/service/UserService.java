package pl.barMate.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.barMate.model.DrinkHistory;
import pl.barMate.model.UserPreferences;
import pl.barMate.model.UserProfile;
import pl.barMate.repository.DrinkHistoryRepository;
import pl.barMate.repository.UserProfileRepository;

import java.util.List;

@Service
public class UserService {

    UserProfileRepository userProfileRepository;
    DrinkHistoryRepository drinkHistoryRepository;

    public void addUserProfile(String keycloakId, String username, String email) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(username);
        userProfile.setEmail(email);
        userProfile.setKeycloakId(keycloakId);
        userProfile.setUserPreferences(new UserPreferences());
        userProfileRepository.save(userProfile);
    }

    public UserProfile getUserProfile(String keycloakId) {
        return userProfileRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserPreferences getUserPreferences(String keycloakId) {
        return userProfileRepository.findByKeycloakId(keycloakId)
                .map(UserProfile::getUserPreferences)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<DrinkHistory> getDrinkHistory(String keycloakId) {
        return userProfileRepository.findByKeycloakId(keycloakId)
                .map(UserProfile::getHistory)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void addFavoriteRecipe(String keycloakId, Long recipeId) {
        UserProfile userProfile = getUserProfile(keycloakId);
        userProfile.getUserPreferences().getFavoriteRecipes().add(recipeId);
        userProfileRepository.save(userProfile);
    }

    public void removeFavoriteRecipe(String keycloakId, Long recipeId) {
        UserProfile userProfile = getUserProfile(keycloakId);
        userProfile.getUserPreferences().getFavoriteRecipes().remove(recipeId);
        userProfileRepository.save(userProfile);
    }

    public void addFavoriteIngredient(String keycloakId, Long ingredientId) {
        UserProfile userProfile = getUserProfile(keycloakId);
        userProfile.getUserPreferences().getFavoriteIngredients().add(ingredientId);
        userProfileRepository.save(userProfile);
    }

    public void removeFavoriteIngredient(String keycloakId, Long ingredientId) {
        UserProfile userProfile = getUserProfile(keycloakId);
        userProfile.getUserPreferences().getFavoriteIngredients().remove(ingredientId);
        userProfileRepository.save(userProfile);
    }

    public void addDrinkHistory(String keycloakId, DrinkHistory drinkHistory) {
        UserProfile user = userProfileRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        drinkHistory.setUser(user);
        drinkHistoryRepository.save(drinkHistory);
    }
}
