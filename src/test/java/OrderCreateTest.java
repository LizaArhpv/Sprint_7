import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Before;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static org.apache.http.HttpStatus.SC_CREATED;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;


@RunWith(Parameterized.class)
public class OrderCreateTest {

    private List<String> color;
    private Integer track;
    private RequestSpecification requestSpec;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Api.BASE_URL;
        requestSpec = given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON);
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Collection<Object[]> getOrderColor() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}
        });
    }

    @After
    public void tearDown() {
        if (track != null && track > 0) {
            try {
                given()
                        .filter(new AllureRestAssured())
                        .queryParam("track", track)
                        .put(Api.ORDERS_PATH + "/cancel");

            } catch (Exception e) {
                System.err.println("Критическая ошибка");
            }
        }
    }
        @Test
        @DisplayName("Создание заказа с разными цветами")
        @Description("Создание заказа возвращает 201 и track в теле ответа")
        public void shouldCreateOrderWithDifferentColorsAndReturnTrack () {
            Order order = Order.defaultOrder(color);

            Response response = requestSpec
                    .body(order)
                    .post(Api.ORDERS_PATH);

            response.then()
                    .statusCode(SC_CREATED)
                    .body("track", notNullValue());

            track = response.jsonPath().getInt("track");

            if (track <= 0) { // int не может быть null, проверяем только на <= 0
                throw new AssertionError("Получен некорректный track: " + track);
            }
        }
    }

