package tests;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import core.Basic;
import io.restassured.RestAssured;

public class TC_DELETE_DeleteBooking {

	String baseURI = "https://restful-booker.herokuapp.com";
	Basic base = new Basic();
	String accessToken = null;
	
	@Before
	public void setup() {
		accessToken = base.generateAccessToken();
	}

	/**
	 * This test help you to verify if we can delete a booking based on booking ID
	 */
	@Test
	public void test_deleteHotelBooking() {
		int bookingId = base.getBookingId();  //Get the booking ID
		
		RestAssured
		 .given()
		   .header("Content-Type", "application/json")
		   .header("Cookie", "token="+accessToken)
		   .pathParam("id", bookingId)
		 .when()
		   .delete(baseURI+"/booking/{id}") //run delete request method for the resource
		 .then()
		   .statusCode(equalTo(201));
		
		verifyDeletedBooking(bookingId);
	}
	
	/**
	 * This method verifies that booking is no longer available once deleted
	 * @param bookingId
	 */
	private void verifyDeletedBooking(int bookingId) {
		RestAssured
		 .given()
		   .header("Accept", "application/json")
		   .pathParam("id", bookingId)
		 .when()
		   .get(baseURI+"/booking/{id}")
		 .then()
		   .statusCode(equalTo(404)); //Not found
		
	}
}
