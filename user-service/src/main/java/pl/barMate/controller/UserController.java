package pl.barMate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.barMate.model.DrinkHistory;
import pl.barMate.model.UserPreferences;
import pl.barMate.model.UserProfile;
import pl.barMate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
        public ResponseEntity<String> getHello() {
        System.out.println("hello");
        return ResponseEntity.ok("hello");
    }

//    @GetMapping
//    public ResponseEntity<String> getUserProfile(
//            @AuthenticationPrincipal OidcUser jwt) {
//        String hello = "Hi, " + jwt.getName();
//        System.out.println(hello);
//        return ResponseEntity.ok(hello);
//    }
//
//    @PostMapping("/favorites/{recipeId}")
//    public ResponseEntity<Void> addFavoriteRecipe(
//            @AuthenticationPrincipal Jwt jwt, @PathVariable Long recipeId) {
//        userService.addFavoriteRecipe(jwt.getSubject(), recipeId);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/favorites/{recipeId}")
//    public ResponseEntity<Void> deleteFavoriteRecipe(
//            @AuthenticationPrincipal Jwt jwt, @PathVariable Long recipeId) {
//        userService.removeFavoriteRecipe(jwt.getSubject(), recipeId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/favorites/{ingredientId}")
//    public ResponseEntity<Void> addFavoriteIngredient(
//            @AuthenticationPrincipal Jwt jwt, @PathVariable Long ingredientId) {
//        userService.addFavoriteIngredient(jwt.getSubject(), ingredientId);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/favorites/{ingredientId}")
//    public ResponseEntity<Void> deleteFavoriteIngredient(
//            @AuthenticationPrincipal Jwt jwt, @PathVariable Long ingredientId) {
//        userService.removeFavoriteIngredient(jwt.getSubject(), ingredientId);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/preferences")
//    public ResponseEntity<UserPreferences> getUserPreferences(
//            @AuthenticationPrincipal Jwt jwt
//    ) {
//        return ResponseEntity.ok(userService.getUserPreferences(jwt.getSubject()));
//    }
//
//    @PostMapping("/history")
//    public ResponseEntity<Void> addDrinkHistory(
//            @AuthenticationPrincipal Jwt jwt, @RequestBody DrinkHistory drinkHistory) {
//        userService.addDrinkHistory(jwt.getSubject(), drinkHistory);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/history")
//    public ResponseEntity<List<DrinkHistory>> getDrinkHistory(
//            @AuthenticationPrincipal Jwt jwt) {
//        return ResponseEntity.ok(userService.getDrinkHistory(jwt.getSubject()));
//    }
}
