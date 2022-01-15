package wadge.dao.impl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.springframework.stereotype.Repository;

import wadge.dao.api.IFridgeDao;
import wadge.model.fridge.FoodElement;
import wadge.model.fridge.FridgeFood;

@Repository("jsonFridgeDao")
public class JsonFridgeDao implements IFridgeDao {
    private final Map<String, FridgeFood> fridge;
    private final ObjectMapper mapper;
    static final String FILE_NAME = "fridge.json";
    private static Logger logger = Logger.getLogger(JsonFridgeDao.class.getName());
    
    public JsonFridgeDao() {
        mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        fridge = new HashMap<>();
        
        try {
            List<FridgeFood> list = Arrays.asList(mapper.readValue(Paths.get(FILE_NAME).toFile(), FridgeFood[].class));
            addAllToFridge(list);
        } catch (IOException e) {
            logger.log(Level.FINE, e.getMessage());
        }
    }
    
    @Override
    public boolean addAllToFridge(List<FridgeFood> food) {
        boolean rtr = food.stream().map(aFood -> insertFridgeFood(aFood)).allMatch(a -> a);
        
        if (rtr) {
            try {
                mapper.writeValue(Paths.get(FILE_NAME).toFile(), fridge.values());
            } catch (IOException e) {
                logger.log(Level.FINE, e.getMessage());
                rtr = false;
            }
        }
        
        return rtr;
    }
    
    @Override
    public List<FridgeFood> getAllFridge() {
        return fridge.values().stream().collect(Collectors.toList());
    }
    
    public Map<String, FridgeFood> getFridge() {
        return fridge;
    }
    
    public FridgeFood getFridgeFood(String name) {
        return fridge.get(name);
    }
    
    @Override
    public boolean insertFridgeFood(UUID id, FridgeFood food) {
        food.setId(1L);
        fridge.put(food.getName(), food);
        return true;
    }
    
    @Override
    public boolean insertFridgeFood(FridgeFood food) {
        fridge.put(food.getName(), food);
        return true;
    }
    
    @Override
    public boolean addFoodElementToFridgeFood(String fridgeFood, FoodElement element) {
        getFridgeFoodFromName(fridgeFood).ifPresent(ff -> {
            ff.addProduct(element);
            try {
                mapper.writeValue(Paths.get(FILE_NAME).toFile(), fridge.values());
            } catch (IOException e) {
                logger.log(Level.FINE, e.getMessage());
            }
        });
        return true;
    }
    
    @Override
    public Optional<FridgeFood> getFridgeFoodFromName(String name) {
        return Optional.ofNullable(fridge.get(name));
    }
    
    @Override
    public void saveData() {
        try {
            mapper.writeValue(Paths.get(FILE_NAME).toFile(), fridge.values());
        } catch (IOException e) {
            logger.log(Level.FINE, e.getMessage());
        }
    }
    
    @Override
    public void deleteFromFridge(String food, UUID id) {
        this.fridge.get(food).getProducts().remove(id);
        saveData();
    }

    @Override
    public void deleteUsingId(Set<Map.Entry<UUID, String>> ids) {
        ids.stream().forEach(entry ->
            fridge.get(entry.getValue()).getProducts().remove(entry.getKey())
        );
        saveData();
    }
}
