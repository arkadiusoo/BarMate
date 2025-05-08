package pl.barMate.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue
    private Long id;

    private String keycloakId;
    private String username;

    @Getter
    @Embedded
    private UserPreferences userPreferences;

    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DrinkHistory> history = new ArrayList<>();

}
