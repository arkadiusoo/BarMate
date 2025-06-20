package pl.barMate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.barMate.model.DrinkHistory;
import pl.barMate.model.UserPreferences;
import pl.barMate.model.UserProfile;
import pl.barMate.repository.DrinkHistoryRepository;
import pl.barMate.repository.UserProfileRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserProfileRepository userProfileRepository;
    final private DrinkHistoryRepository drinkHistoryRepository;
    final private RestTemplate restTemplate;

    public void addUserProfile(Long keycloakId, String username, String email) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(username);
        userProfile.setEmail(email);
        userProfile.setUserId(keycloakId);
        userProfile.setUserPreferences(new UserPreferences());
        userProfileRepository.save(userProfile);
    }

    public UserProfile getUserProfile(String username) {
        return userProfileRepository.findUserProfileByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserPreferences getUserPreferences(String username) {
        return userProfileRepository.findUserProfileByUsername(username)
                .map(UserProfile::getUserPreferences)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<DrinkHistory> getDrinkHistory(String username) {
        return userProfileRepository.findUserProfileByUsername(username)
                .map(UserProfile::getHistory)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void addFavoriteRecipe(String username, Long recipeId) {
        UserProfile userProfile = getUserProfile(username);
        userProfile.getUserPreferences().getFavoriteRecipes().add(recipeId);
        userProfileRepository.save(userProfile);
    }

    public void removeFavoriteRecipe(String username, Long recipeId) {
        UserProfile userProfile = getUserProfile(username);
        userProfile.getUserPreferences().getFavoriteRecipes().remove(recipeId);
        userProfileRepository.save(userProfile);
    }

    public void addFavoriteIngredient(String username, String ingredientId) {
        UserProfile userProfile = getUserProfile(username);
        userProfile.getUserPreferences().getFavoriteIngredients().add(ingredientId);
        userProfileRepository.save(userProfile);
    }

    public void removeFavoriteIngredient(String username, String ingredientId) {
        UserProfile userProfile = getUserProfile(username);
        userProfile.getUserPreferences().getFavoriteIngredients().remove(ingredientId);
        userProfileRepository.save(userProfile);
    }

    public void addDrinkHistory(String username, DrinkHistory drinkHistory) {
        UserProfile user = userProfileRepository.findUserProfileByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        drinkHistory.setUser(user);
        drinkHistoryRepository.save(drinkHistory);
    }

    public List<String> getNameDrinkHistory(String username) {
        List<DrinkHistory> drinkHistoryList = drinkHistoryRepository.findByUser_Username(username);
        List<Long> recipeIds = drinkHistoryList.stream()
                .map(DrinkHistory::getRecipeId)
                .toList();
        List<String> drinkNames = new ArrayList<>();
        for (Long recipeId : recipeIds) {
            try {
                Map<String, Object> cocktail = restTemplate.getForObject(
                        "http://drink-service/api/cocktails/lookup?id=" + recipeId,
                        Map.class
                );
                if (cocktail != null && cocktail.get("name") != null) {
                    drinkNames.add(cocktail.get("name").toString());
                }
            } catch (Exception e) {
                System.err.println("Nie udało się pobrać danych dla ID: " + recipeId);
            }
        }
        return drinkNames;
    }

    public List<String> getIngredientHistory(String username) {
        List<DrinkHistory> drinkHistoryList = drinkHistoryRepository.findByUser_Username(username);
        List<Long> recipeIds = drinkHistoryList.stream()
                .map(DrinkHistory::getRecipeId)
                .toList();
        List<String> drinkIngredients = new ArrayList<>();
        for (Long recipeId : recipeIds) {
            try {
                Map<String, Object> cocktail = restTemplate.getForObject(
                        "http://drink-service/api/cocktails/lookup?id=" + recipeId,
                        Map.class
                );

                if (cocktail != null && cocktail.get("ingredients") instanceof List<?> ingredientsList) {
                    for (Object ingredient : ingredientsList) {
                        if (ingredient != null) {
                            drinkIngredients.add(ingredient.toString());
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Nie udało się pobrać danych dla ID: " + recipeId);
            }
        }
        return drinkIngredients;
    }

    public Map<String, List<String>> getDrinksAndDates(String username) {
        List<DrinkHistory> drinkHistoryList = drinkHistoryRepository.findByUser_Username(username);

        Map<String, List<String>> nameDateMap = new LinkedHashMap<>();

        for (DrinkHistory entry : drinkHistoryList) {
            Long recipeId = entry.getRecipeId();
            String date = entry.getDate().toString();

            try {
                Map<String, Object> cocktail = restTemplate.getForObject(
                        "http://drink-service/api/cocktails/lookup?id=" + recipeId,
                        Map.class
                );

                if (cocktail != null && cocktail.get("name") != null) {
                    String name = cocktail.get("name").toString();
                    nameDateMap.computeIfAbsent(name, k -> new ArrayList<>()).add(date);
                }

            } catch (Exception e) {
                System.err.println("Błąd przy pobieraniu danych dla ID: " + recipeId);
            }
        }

        return nameDateMap;
    }
}