import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private CourierAuthorizationTest courierAuthorizationTest;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierAuthorizationTest = new CourierAuthorizationTest();
        courier = GeneratorForCourier.getRandomCourier();
        courierAuthorizationTest.createCourier(courier);
    }

    @After
    public void tearDown() {
        if (courier != null) {
            Response loginResponse = courierAuthorizationTest.loginCourier(CourierAuthorization.from(courier));
            if (loginResponse.statusCode() == SC_OK && loginResponse.jsonPath().get("id") != null) {
                courierId = loginResponse.jsonPath().getInt("id");
                courierAuthorizationTest.deleteCourier(courierId);
            }
        }
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Успешная авторизация возвращает 200 и id")
    public void shouldLoginCourierSuccessfullyAndReturnId() {
        Response response = courierAuthorizationTest.loginCourier(CourierAuthorization.from(courier));
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка авторизации без логина")
    @Description("Авторизация без логина возвращает 400")
    public void shouldReturnErrorWhenLoginIsMissing() {
        Response response = courierAuthorizationTest.loginCourierWithoutLogin(courier.getPassword());
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @DisplayName("Ошибка авторизации без пароля")
    @Description("Авторизация без пароля возвращает 504")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        Response response = courierAuthorizationTest.loginCourierWithoutPassword(courier.getLogin());
        response.then()
                .statusCode(SC_GATEWAY_TIMEOUT);
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным логином")
    @Description("Авторизация с неверным логином возвращает 404")
    public void shouldReturnErrorWithWrongLogin() {
        CourierAuthorization credentials = new CourierAuthorization(
                "wrongLogin_" + System.currentTimeMillis(),
                courier.getPassword()
        );
        Response response = courierAuthorizationTest.loginCourier(credentials);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным паролем")
    @Description("Авторизация с неверным паролем возвращает 404")
    public void shouldReturnErrorWithWrongPassword() {
        CourierAuthorization credentials = new CourierAuthorization(
                courier.getLogin(),
                "wrongPassword_" + System.currentTimeMillis()
        );
        Response response = courierAuthorizationTest.loginCourier(credentials);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации несуществующего курьера")
    @Description("Авторизация несуществующего курьера возвращает 404")
    public void shouldReturnErrorForNonexistentCourier() {
        CourierAuthorization credentials = new CourierAuthorization(
                "notExisting_" + System.currentTimeMillis(),
                "notExisting_" + System.currentTimeMillis()
        );
        Response response = courierAuthorizationTest.loginCourier(credentials);
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("не найдена"));
    }
}
