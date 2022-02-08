package wadge.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import java.time.Month;
import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import wadge.service.food.FoodService;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DataMongoTest
class FoodControllerTest {
    @Mock
    private FoodService foodService;
    private FoodController underTest;

    static final List<String> monthList = List.of("JANUARY", "FEBRUARY", "MARCH",
            "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "DECEMBER");

    @BeforeEach
    void setUp() {
        underTest = new FoodController(foodService);
    }

    @Test
    void getAllFood() {
        underTest.getAllFood();
        verify(foodService).getAllFood();
    }

    @Test
    void getFoodFromMonth(){
        monthList.stream().forEach(monthAsString -> {
            final Month month = Month.valueOf(monthAsString) ;
            underTest.getFoodFromMonth(monthAsString);
            verify(foodService).getFoodFromGivenMonth(month);
        });
    }

    // TODO Add values to improve tests

    @Test
    void getFoodFromMonthException() {
        assertEquals(List.of(), underTest.getFoodFromMonth("test"));
    }

    @Test
    @Ignore
    void getFoodFromMonthByDays(){
    }

    @Test
    @Ignore
    void convert() {
    }
}
