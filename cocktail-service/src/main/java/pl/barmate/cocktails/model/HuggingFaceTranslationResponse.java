package pl.barmate.cocktails.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HuggingFaceTranslationResponse {
    @JsonProperty("translation_text")
    private String translationText;
}