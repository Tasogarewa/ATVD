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

public class UserTest {
    private static final String baseUrl = "https://petstore.swagger.io/v2";
private static final String USER ="/user",
    USER_USERNAME = USER +"/{username}",
        USER_LOGIN = USER +"/login",
    USER_LOGOUT = USER +"/logout";
private String username;
private String firstName;
    @BeforeClass
        public void setup()
    {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }
   @Test
   public void verifyLoginAction()
   {
       Map<String,?> body = Map.of("username", "ArtemShafranovskiy","password", "122-22-sk1");
    Response response = given().body(body).get(USER_LOGIN);
    response.then().statusCode(HttpStatus.SC_OK);
       String message = response.jsonPath().getString("message");
       String sessionId = message.replaceAll("[^0-9]", "");
       RestAssured.requestSpecification.sessionId(sessionId);
   }
    @Test
        public void verifyCreateAction()
    {

        username = Faker.instance().name().firstName();
        System.out.println("Created user: " + username);
        firstName = Faker.instance().harryPotter().character();
        Map<String,?> body = Map.of("username", username,"firstName", firstName,"lastName",Faker.instance().gameOfThrones().character(),"email",Faker.instance().internet().emailAddress(),"password",Faker.instance().internet().password(),"phone", Faker.instance().phoneNumber().phoneNumber(),"userStatus",Integer.valueOf("1"));
    given().body(body).post(USER).then().statusCode(HttpStatus.SC_OK);
    }
    @Test(dependsOnMethods = "verifyCreateAction")
    public void verifyGetAction()
    {
        System.out.println(username);
        given().pathParam("username", username).get(USER_USERNAME).then().statusCode(HttpStatus.SC_OK).and().body("firstName",equalTo(firstName));

    }
    @Test(dependsOnMethods = "verifyGetAction")
    public void verifyDeleteAction()
    {
        given().pathParam("username", username).delete(USER_USERNAME).then().statusCode(HttpStatus.SC_OK);
    }
    @Test(dependsOnMethods = "verifyLoginAction",priority = 1)
    public void verifyLogoutAction()
    {
        given().get(USER_LOGOUT).then().statusCode(HttpStatus.SC_OK);
    }

}
