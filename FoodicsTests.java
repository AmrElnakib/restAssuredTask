import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FoodicsTests {
    private String token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://pay2.foodics.dev";
        RestAssured.basePath = "/cp_internal";
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"email\": \"merchant@foodics.com\",\"password\": \"123456\"}")
                .post("/login");
        token = response.jsonPath().getString("token");
    }

    @Test
    public void testLoginEndpoint() {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"email\": \"merchant@foodics.com\",\"password\": \"123456\"}")
                .post("/login");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

    @Test
    public void testWhoAmIEndpoint() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/whoami");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String email = response.getBody().jsonPath().getString("email");
        Assert.assertEquals(email, "merchant@foodics.com");
    }

    @Test
    public void testInvalidToken() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer invalidtoken")
                .get("/whoami");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
    }
}