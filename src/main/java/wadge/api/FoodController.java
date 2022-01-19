package wadge.api;

import java.time.Month;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wadge.model.food.ConversionRequest;
import wadge.model.food.Food;
import wadge.service.food.FoodService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping(path="/foods")
    public List<Food> getAllFood() {
        return foodService.getAllFood();
    }

    @GetMapping(path="/foods/{month}")
    public List<Food> getFoodFromMonth(@PathVariable String month) {
        if (month.length() != 0) {
            month = month.toUpperCase();
        }
       return foodService.getFoodFromGivenMonth(Month.valueOf(month));
    }

    @GetMapping(path="/foods/{month}/days")
    public List<Food> getFoodFromMonthByDays(@PathVariable String month) {
        if (month.length() != 0) {
            month = month.toUpperCase();
        }
       return foodService.sortByDays(foodService.getFoodFromGivenMonth(Month.valueOf(month)));
    }  

    @PostMapping(path="/foods/scale") 
    public Optional<Double> convert(@RequestBody final JsonNode node) {
        final ConversionRequest request = mapper.convertValue(node, ConversionRequest.class);
        return foodService.convert(request);
    }
}
