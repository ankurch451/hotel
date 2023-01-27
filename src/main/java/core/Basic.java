package core;
import static org.hamcrest.Matchers.equalTo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojos.Token;

/**
 * This class is created to store some common methods used during automation
 * @author ANKUR
 *
 */
public class Basic {

	String baseURI = "https://restful-booker.herokuapp.com";
	String accessToken = null;
	
	/**
	 * This method generates access token
	 * @return
	 */
	public String generateAccessToken() {
		Token dataForToken = new Token();
		String username = "admin";
		String password = "password123";
		dataForToken.setUsername(username);
		dataForToken.setPassword(password);
		ObjectMapper mapper =  new ObjectMapper();
		String data=null;
		try {
			data = mapper.writeValueAsString(dataForToken);
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
	 Response res = RestAssured
		.given()
		  .header("Content-Type","application/json")
		  .body(data)
		.when()
		  .post(baseURI+"/auth");
		accessToken = res.body().jsonPath().getString("token");
		return accessToken;
		
	}
	
	/**
	 * This method returns you the booking ID for the first resource
	 * @return booking ID
	 */
	public int getBookingId() {
		Response res = RestAssured
		 .when()
		   .get(baseURI+"/booking")
		 .then()
		   .statusCode(equalTo(200))
		   .extract().response();
		
		return res.jsonPath().getInt("bookingid[0]");
	}
}
