package unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.barMate.model.UserPreferences;
import pl.barMate.model.UserProfile;
import pl.barMate.repository.DrinkHistoryRepository;
import pl.barMate.repository.UserProfileRepository;
import pl.barMate.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserProfileRepository userProfileRepository;
    private DrinkHistoryRepository drinkHistoryRepository;
    private UserService userService;

    private final String username = "kamil";

    @BeforeEach
    void setUp() {
        userProfileRepository = mock(UserProfileRepository.class);
        drinkHistoryRepository = mock(DrinkHistoryRepository.class);
        userService = new UserService(userProfileRepository, drinkHistoryRepository, null);

        UserPreferences preferences = new UserPreferences();
        preferences.setFavoriteIngredients(new ArrayList<String>());
        UserProfile profile = new UserProfile();
        profile.setUsername(username);
        profile.setUserPreferences(preferences);

        when(userProfileRepository.findUserProfileByUsername(username))
                .thenReturn(Optional.of(profile));
    }

    @Test
    void shouldAddFavoriteIngredient() {
        userService.addFavoriteIngredient(username, "rum");

        UserProfile user = userService.getUserProfile(username);
        assertThat(user.getUserPreferences().getFavoriteIngredients())
                .contains("rum");

        verify(userProfileRepository).save(user);
    }

    @Test
    void shouldRemoveFavoriteIngredient() {
        userService.addFavoriteIngredient(username, "rum");
        userService.removeFavoriteIngredient(username, "rum");

        UserProfile user = userService.getUserProfile(username);
        assertThat(user.getUserPreferences().getFavoriteIngredients())
                .doesNotContain("rum");

        verify(userProfileRepository, times(2)).save(user);
    }

    @Test
    void shouldReturnUserPreferences() {
        UserPreferences prefs = userService.getUserPreferences(username);
        assertThat(prefs).isNotNull();
        assertThat(prefs.getFavoriteIngredients()).isEmpty();
    }

    @Test
    void shouldAddFavoriteRecipe() {
        userService.addFavoriteRecipe(username, 42L);

        UserProfile user = userService.getUserProfile(username);
        assertThat(user.getUserPreferences().getFavoriteRecipes())
                .contains(42L);

        verify(userProfileRepository).save(user);
    }

    @Test
    void shouldRemoveFavoriteRecipe() {
        userService.addFavoriteRecipe(username, 42L);
        userService.removeFavoriteRecipe(username, 42L);

        UserProfile user = userService.getUserProfile(username);
        assertThat(user.getUserPreferences().getFavoriteRecipes())
                .doesNotContain(42L);

        verify(userProfileRepository, times(2)).save(user);
    }

    @Test
    void shouldReturnUserProfileWhenExists() {
        UserProfile profile = userService.getUserProfile(username);
        assertThat(profile).isNotNull();
        assertThat(profile.getUsername()).isEqualTo(username);
    }

    @Test
    void shouldThrowWhenUserProfileNotFound() {
        when(userProfileRepository.findUserProfileByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserProfile("unknown"))
                .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldAddDrinkHistory() {
        var drinkHistoryRepo = mock(DrinkHistoryRepository.class);
        userService = new UserService(userProfileRepository, drinkHistoryRepo, null);

        var drink = new pl.barMate.model.DrinkHistory();
        drink.setRecipeId(101L);
        userService.addDrinkHistory(username, drink);

        assertThat(drink.getUser()).isNotNull();
        assertThat(drink.getUser().getUsername()).isEqualTo(username);

        verify(drinkHistoryRepo).save(drink);
    }

    @Test
    void shouldReturnDrinkHistory() {
        var user = userService.getUserProfile(username);

        var historyEntry = new pl.barMate.model.DrinkHistory();
        historyEntry.setRecipeId(123L);
        var history = List.of(historyEntry);
        user.setHistory(history);

        when(userProfileRepository.findUserProfileByUsername(username))
                .thenReturn(Optional.of(user));

        var result = userService.getDrinkHistory(username);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipeId()).isEqualTo(123L);
    }
}
