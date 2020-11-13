/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package wadge.backend;

import org.testng.annotations.*;
import wadge.food_list.FoodList;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class AppTest {
    List<Map<String, Object>> list;
    List<Map<String, Object>> empty;

    @Test public void appHasAGreeting() {
        // App classUnderTest = new App();
        // assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }

    @BeforeMethod
    public void setUp(){
        list = new ArrayList<>();
        list = FoodList.readFile("./src/test/resources/food_list_test.json");
    }
    @AfterMethod
    public void tearDown() {
        list = null;
        empty = null;
    }
    @Test
    public void testGoodReadFile(){
        System.out.println(list);
        System.out.println(FoodList.readFile("./src/test/resources/food_list_test.json"));
        assertEquals(FoodList.readFile("./src/test/resources/food_list_test.json"),list);
    }
    @Test
    public void testBadStructureReadFile(){
        assertNotEquals(FoodList.readFile("./src/test/resources/food_test_bad_structure.json"),list);

    }
//    @Test
//    public void testNoFile() {
//            assertEquals(FoodList.readFile("./src/test/resources/food_list_empty.json"),empty);
//    }

}