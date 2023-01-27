package tests;

import static org.hamcrest.Matchers.equalTo;
import org.hamcrest.Matchers;
import org.junit.Test;

import core.Basic;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class TC_GET_BookingIds {
	Basic base = new Basic();
	String baseURI = "https://restful-booker.herokuapp.com";
	
	
	/**
	 * This method tests if we can  retrieve bookings without applying any filter
	 */
	@Test
	public void test_getBookingID_withoutFilter() {
	  int bookingId = base.getBookingId();
	  RestAssured
		 .when()
		   .get(baseURI+"/booking")
		 .then()
		   .statusCode(equalTo(200))
		   .header("Content-Length", Matchers.not(Matchers.isEmptyOrNullString()))
		   .body("bookingid", Matchers.hasItem(bookingId)); // verify if Booking ID is present in the list
	}
	
	/**
	 * This method tests if we can retrieve bookings by applying filters
	 */
	@Test
	public void test_getBookingId_withFilter() {
		int bookingId = base.getBookingId();
		JsonPath path = RestAssured.get(baseURI+"/booking/"+bookingId).then().extract().response().getBody().jsonPath();
		String firstName = path.getString("firstname");
		String lastName = path.getString("lastname");
	
	    RestAssured.given()
				 .when()
				   .queryParam("firstname", firstName)
				   .queryParam("lastname", lastName)
				   .get(baseURI+"/booking")
				 .then()
				   .statusCode(equalTo(200))
	               .body("bookingid", Matchers.hasItem(bookingId));
	}

}
