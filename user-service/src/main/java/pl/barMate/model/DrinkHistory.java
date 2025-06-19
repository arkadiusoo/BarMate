package pl.barMate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class DrinkHistory {
    @Id
    @GeneratedValue
    private Long id;

    private Long recipeId;
    private LocalDateTime date;
    private int rating;
    private String comment;

    @ManyToOne
    private UserProfile user;
}
