package pl.barmate.cocktails.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CocktailDeserializer extends StdDeserializer<Cocktail> {
    public CocktailDeserializer() { super(Cocktail.class); }

    @Override
    public Cocktail deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        Cocktail c = new Cocktail();
        c.setId(node.path("idDrink").asText());
        c.setName(node.path("strDrink").asText());
        List<String> ing = new ArrayList<>();
        List<String> meas = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            JsonNode ni = node.path("strIngredient"+i);
            JsonNode nm = node.path("strMeasure"+i);
            if (!ni.isNull() && !ni.asText().isBlank()) {
                ing.add(ni.asText());
                meas.add(nm.isNull()? "" : nm.asText());
            }
        }
        c.setIngredients(ing);
        c.setMeasures(meas);
        return c;
    }
}

