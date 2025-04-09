    package lab3;


    import io.restassured.RestAssured;
    import io.restassured.builder.RequestSpecBuilder;
    import io.restassured.builder.ResponseSpecBuilder;
    import io.restassured.http.ContentType;
    import org.apache.hc.core5.http.HttpStatus;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;

    import static io.restassured.RestAssured.given;
    import static org.hamcrest.CoreMatchers.equalTo;

    public class MockServerTest {

        private static final String BASE_URL = "https://125a99a6-2c1a-4555-93a6-c97495b322f9.mock.pstmn.io";
        private static final String OWNER_NAME_UNSUCCESS = "/ownerName/unsuccess";
        private static final String OWNER_NAME_SUCCESS = "/ownerName/success";
        private static final String CREATE_SOMETHING_WITH_PERMISSION = "/createSomething?permission=yes";
        private static final String CREATE_SOMETHING_NO_PERMISSION = "/createSomething";
        private static final String UPDATE_ME = "/updateMe";
        private static final String DELETE_WORLD = "/deleteWorld";

        @BeforeClass
        public void setup() {
            RestAssured.baseURI = BASE_URL;
            RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
            RestAssured.responseSpecification = new ResponseSpecBuilder().build();
        }

        @Test
        public void verifyUnsuccessfulOwnerName() {
            given().get(OWNER_NAME_UNSUCCESS)
                    .then().statusCode(HttpStatus.SC_FORBIDDEN)
                    .and().body("exception", equalTo("I won't say my name"));
        }

        @Test
        public void verifySuccessfulOwnerName() {
            given().get(OWNER_NAME_SUCCESS)
                    .then().statusCode(HttpStatus.SC_OK)
                    .and().body("name", equalTo("A.Sh"));
        }

        @Test
        public void verifyCreateSomethingWithPermission() {
            given().post(CREATE_SOMETHING_WITH_PERMISSION)
                    .then().statusCode(HttpStatus.SC_OK)
                    .and().body("result", equalTo("'Nothing' was created"));
        }

        @Test
        public void verifyCreateSomethingWithoutPermission() {
            given().post(CREATE_SOMETHING_NO_PERMISSION)
                    .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                    .and().body("result", equalTo("You don't have permission to create Something"));
        }

        @Test
        public void verifyUpdateMe() {
            given().put(UPDATE_ME)
                    .then().statusCode(HttpStatus.SC_SERVER_ERROR)
                    .and().body("name", equalTo(null))
                    .and().body("surname", equalTo(null));
        }

        @Test
        public void verifyDeleteWorld() {
            given().delete(DELETE_WORLD)
                    .then().statusCode(HttpStatus.SC_GONE)
                    .and().body("world", equalTo("0"));
        }
    }