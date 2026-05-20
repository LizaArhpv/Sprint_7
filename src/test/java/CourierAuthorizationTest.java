import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
    public Response loginCourier(CourierAuthorization credentials) { // ИЗМЕНЕНО: теперь принимает CourierAuthorization
        return given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON)
                .body(credentials)
                .post(Api.COURIER_LOGIN_PATH);
    }

    @Step("Авторизоваться курьером без логина")
    public Response loginCourierWithoutLogin(String password) {
        return given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON)
                .body(Map.of("password", password))
                .post(Api.COURIER_LOGIN_PATH);
    }

    @Step("Авторизоваться курьером без пароля")
    public Response loginCourierWithoutPassword(String login) {
        return given()
                .filter(new AllureRestAssured())
                .header(Api.CONTENT_TYPE, Api.APPLICATION_JSON)
                .body(Map.of("login", login))
                .post(Api.COURIER_LOGIN_PATH);
    }

    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return given()
                .filter(new AllureRestAssured())
                .delete(Api.COURIER_PATH + "/" + courierId);
    }

    @Step("Удалить курьера без id")
    public Response deleteCourierWithoutId() {
        return given()
                .filter(new AllureRestAssured())
                .delete(Api.COURIER_PATH);
    }
}
