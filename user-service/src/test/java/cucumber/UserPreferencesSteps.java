package cucumber;

import static org.assertj.core.api.Assertions.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pl.barMate.model.UserPreferences;
import pl.barMate.model.UserProfile;
import pl.barMate.repository.UserProfileRepository;
import pl.barMate.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserPreferencesSteps {

    private UserService userService;
    private UserProfile testUser;
    private UserProfileRepository userProfileRepository;

    @Given("a user profile with username {string} exists")
    public void givenUserProfile(String username) {
        userProfileRepository = mock(UserProfileRepository.class);
        userService = new UserService(userProfileRepository, null, null);

        UserPreferences preferences = new UserPreferences();
        preferences.setFavoriteIngredients(new ArrayList<String>());
        testUser = new UserProfile();
        testUser.setUsername(username);
        testUser.setUserPreferences(preferences);

        when(userProfileRepository.findUserProfileByUsername(username))
                .thenReturn(Optional.of(testUser));
        when(userProfileRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
    }

    @When("the user adds the ingredient {string} to favorites")
    public void whenAddFavoriteIngredient(String ingredientId) {
        userService.addFavoriteIngredient(testUser.getUsername(), ingredientId);
    }

    @Then("the favorite ingredients of user {string} should contain {string}")
    public void thenFavoritesShouldContain(String username, String ingredientId) {
        assertThat(testUser.getUserPreferences().getFavoriteIngredients())
                .contains(ingredientId);
    }
}
