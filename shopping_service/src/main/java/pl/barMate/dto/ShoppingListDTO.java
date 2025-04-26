package pl.barMate.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object representing a shopping list")
public class ShoppingListDTO {

    @Schema(description = "Unique identifier of the shopping list", example = "1")
    private Long id;

    @Schema(description = "Identifier of the user who created the shopping a list", example = "1")
    private Long userId;

    @Schema(description = "List of items on the shopping list", example = "[[1, 'Flour', 2, 'kg', 'true', 2]]")
    private List<ShoppingItemDTO> items;

    @Schema(description = "Time of creation of a list", example = "2025-04-26")
    private LocalDate createdAt;
}
