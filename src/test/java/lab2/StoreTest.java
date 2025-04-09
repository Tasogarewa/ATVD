package lab2;


import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class StoreTest {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String STORE_ORDER = "/store/order";
    private static final String STORE_ORDER_BY_ID = STORE_ORDER + "/{orderId}";

    private int orderId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyCreateOrder() {
        orderId = 12222; // Номер групи + номер студента
        String studentName = "A.Sh"; // Ініціали

        Map<String, ?> body = Map.of(
                "id", orderId,
                "petId", 10,
                "quantity", 1,
                "shipDate", "2024-03-23T10:00:00.000Z",
                "status", "placed",
                "complete", true
        );

        given().body(body).post(STORE_ORDER)
                .then().statusCode(HttpStatus.SC_OK)
                .and().body("id", equalTo(orderId))
                .and().body("status", equalTo("placed"));
    }

    @Test(dependsOnMethods = "verifyCreateOrder")
    public void verifyGetOrder() {
        given().pathParam("orderId", orderId).get(STORE_ORDER_BY_ID)
                .then().statusCode(HttpStatus.SC_OK)
                .and().body("id", equalTo(orderId));
    }

    @Test(dependsOnMethods = "verifyGetOrder")
    public void verifyUpdateOrder() {
        Map<String, ?> updatedBody = Map.of(
                "id", orderId,
                "petId", 10,
                "quantity", 2,
                "shipDate", "2024-03-24T10:00:00.000Z",
                "status", "delivered",
                "complete", true
        );

        given().body(updatedBody).put(STORE_ORDER_BY_ID, orderId)
                .then().statusCode(HttpStatus.SC_OK)
                .and().body("quantity", equalTo(2))
                .and().body("status", equalTo("delivered"));
    }
}
