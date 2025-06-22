package pl.barMate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.barMate.model.DrinkHistory;
import pl.barMate.model.UserPreferences;
import pl.barMate.model.UserProfile;
import pl.barMate.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserProfile> getUserProfile(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        UserProfile userProfile = userService.getUserProfile(username);
        return ResponseEntity.ok(userProfile);
    }

    @PostMapping("/favorites/recipe/{recipeId}")
    public ResponseEntity<Void> addFavoriteRecipe(
            @AuthenticationPrincipal Jwt principal, @PathVariable Long recipeId) {
        String username = principal.getClaimAsString("preferred_username");
        userService.addFavoriteRecipe(username, recipeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/recipe/{recipeId}")
    public ResponseEntity<Void> deleteFavoriteRecipe(
            @AuthenticationPrincipal Jwt principal, @PathVariable Long recipeId) {
        String username = principal.getClaimAsString("preferred_username");
        userService.removeFavoriteRecipe(username, recipeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favorites/ingredient/{ingredientId}")
    public ResponseEntity<Void> addFavoriteIngredient(
            @AuthenticationPrincipal Jwt principal, @PathVariable String ingredientId) {
        String username = principal.getClaimAsString("preferred_username");
        userService.addFavoriteIngredient(username, ingredientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/ingredient/{ingredientId}")
    public ResponseEntity<Void> deleteFavoriteIngredient(
            @AuthenticationPrincipal Jwt principal, @PathVariable String ingredientId) {
        String username = principal.getClaimAsString("preferred_username");
        userService.removeFavoriteIngredient(username, ingredientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/preferences")
    public ResponseEntity<UserPreferences> getUserPreferences(
           @AuthenticationPrincipal Jwt principal
    ) {
        String username = principal.getClaimAsString("preferred_username");
        return ResponseEntity.ok(userService.getUserPreferences(username));
    }

    @PostMapping("/history")
    public ResponseEntity<Void> addDrinkHistory(
            @AuthenticationPrincipal Jwt principal, @RequestBody DrinkHistory drinkHistory) {
        String username = principal.getClaimAsString("preferred_username");
        userService.addDrinkHistory(username, drinkHistory);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<DrinkHistory>> getDrinkHistory(
            @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        return ResponseEntity.ok(userService.getDrinkHistory(username));
    }

    @GetMapping("/getDrinkNames")
    public ResponseEntity<List<String>> getNameDrinkHistory(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        return ResponseEntity.ok(userService.getNameDrinkHistory(username));
    }

    @GetMapping("/getIngredients")
    public ResponseEntity<List<String>> getDrinkHistoryIng(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        return ResponseEntity.ok(userService.getIngredientHistory(username));
    }

    @GetMapping("/getDateList")
    public ResponseEntity<Map<String, List<String>>> getDrinkHistoryList(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        return ResponseEntity.ok(userService.getDrinksAndDates(username));
    }
}
