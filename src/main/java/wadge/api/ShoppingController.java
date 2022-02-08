package wadge.api;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wadge.model.recipe.Ingredient;
import wadge.service.shopping.ShoppingService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class ShoppingController {
    private final ShoppingService shoppingService;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping(path="/shopping_list")
    public Set<Ingredient> getShoppingList(){
        return shoppingService.getShoppingList();
    }

    @PostMapping(path="/shopping_list")
    public Set<Ingredient> addToShoppingList(@RequestBody final JsonNode node) {
        final Set<Ingredient> elements = Set.of(mapper.convertValue(node, Ingredient[].class));
        return shoppingService.addToShoppingList(elements);
    }

    @DeleteMapping(path="/shopping_list")
    public Set<Ingredient> deleteFromShoppingList(@RequestBody final JsonNode node) {
        final Set<String> elements = Set.of(mapper.convertValue(node, String[].class));
        return shoppingService.deleteFromShoppingList(elements);
    }
} 
