package pl.barMate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class UserProfile {
    @Id
    @GeneratedValue
    private Long id;

    private String keycloakId;
    private String username;
    private String email;

    @Getter
    @Embedded
    private UserPreferences userPreferences;

    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DrinkHistory> history = new ArrayList<>();

}
