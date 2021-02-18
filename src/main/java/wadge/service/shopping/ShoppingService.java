package wadge.service.shopping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wadge.model.food.Food;
import wadge.model.recipe.Ingredient;
import wadge.model.recipe.Ingredient.Unit;
import wadge.service.food.FoodService;
import wadge.service.food.FoodHelper;
import wadge.service.food.FoodHelper.Conversion;

@Service
public class ShoppingService {
     Map<String, Ingredient> shoppingList = new HashMap<>();
    private FoodService foodService;

    @Autowired
    public ShoppingService(FoodService foodService) {
        this.foodService = foodService;
    }

    public Set<Ingredient> getShoppingList() {
        return shoppingList.values().stream().collect(Collectors.toSet());
    }
    
    public Set<Ingredient> addToShoppingList(Set<Ingredient> elements) {
        elements.stream().forEach(element -> {
            // The element is contained in food_list or not
            Optional<Food> food = foodService.getFoodFromString(Ingredient.extractName(element));
            String previousName = element.getName();
            if(food.isPresent()) {
                element.setName(food.get().getName());
            }
            System.out.println("Name " + element.getName() + " " + previousName);

            // The name has been changed if is in food_list
            Unit unit = Ingredient.getUnit(previousName);
            double quantity = 0;
            // The element's unit is Kg, g or neither one
            if(!unit.equals(Unit.NONE)) {
                    quantity = Double.parseDouble(element.getQuantity());
                    quantity = unit.equals(Unit.G) ? quantity : quantity * 1000;
                    quantity = FoodHelper.convert(Conversion.G_TO_UNIT, food.get(), quantity);
            } 

            // Adds to the previous value if existed
            Ingredient previousValue = shoppingList.get(element.getName());
            System.out.println("Value" + previousValue);
            if(previousValue != null) {
                System.out.println("Previous " + Double.parseDouble(previousValue.getQuantity()));
                System.out.println("Sum " + Double.parseDouble(previousValue.getQuantity()) + quantity);
                element.setQuantity(String.format("%.1f", Double.parseDouble(previousValue.getQuantity()) + quantity));
            }

            System.out.println("Element" + element);

            shoppingList.put(element.getName(), element);
        });

        return getShoppingList();
    }

}
