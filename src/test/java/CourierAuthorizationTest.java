import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierAuthorizationTest {

    static {
        RestAssured.baseURI = Api.BASE_URL;
    }

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON)
                .body(courier)
                .post(Api.COURIER_PATH);
    }

    @Step("Авторизоваться курьером")
    public Response loginCourier(CourierAuthorization credentials) {
        return given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON)
                .body(credentials)
                .post(Api.COURIER_LOGIN_PATH);
    }

    @Step("Авторизоваться курьером без логина")
    public Response loginCourierWithoutLogin(String password) {
        Map<String, String> body = new HashMap<>();
        body.put("password", password);

        return given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON)
                .body(body)
                .post(Api.COURIER_LOGIN_PATH);
    }

    @Step("Авторизоваться курьером без пароля")
    public Response loginCourierWithoutPassword(String login) {
        Map<String, String> body = new HashMap<>();
        body.put("login", login);

        return given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON)
                .body(body)
                .post(Api.COURIER_LOGIN_PATH);
    }

    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return given()
                .filter(new AllureRestAssured())
                .delete(Api.COURIER_PATH + "/" + courierId);
    }
}

