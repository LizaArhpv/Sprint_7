import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.Before;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.SC_OK;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

public class OrderListTest {

    private Response response;

    @Before
    public void setUp() {

        RestAssured.baseURI = Api.BASE_URL;
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Запрос списка заказов возвращает 200 и список orders")
    public void shouldReturnOrdersList() {
        response = given()
                .filter(new AllureRestAssured())
                .get(Api.ORDERS_PATH);
        response.then()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }
}
