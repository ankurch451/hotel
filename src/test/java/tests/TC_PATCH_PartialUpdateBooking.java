package tests;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.Basic;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojos.BookingData;


public class TC_PATCH_PartialUpdateBooking {
	private String baseURI = "https://restful-booker.herokuapp.com";
	Basic base = new Basic();
	private String accessToken = null;
	ObjectMapper mapper = new ObjectMapper();
	BookingData bookingData ;
	
	
	
	@Before
	public void setup() {
		accessToken = base.generateAccessToken();
	}

	/**
	 * This test method verifies if we can partially update a resource
	 */
	@Test()
	public void test_partialBooking() {
	    int bookingId = base.getBookingId();// get a booking ID in-order to update it.
	    getBookingData(bookingId);
	    
	    //Values to update in a patch request
		String firstname = "Rahul";
		String lastname = "Kumar";
		bookingData.setFirstname(firstname);
		bookingData.setLastname(lastname);
		
		String data = null;
		
		try {
			data = mapper.writeValueAsString(bookingData);//create JSON string using Jackson library
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Response res = RestAssured
		 .given()
		   .header("Content-Type", "application/json")
		   .header("Accept", "application/json")
		   .header("Cookie", "token="+accessToken)
		   .pathParam("id", bookingId)
		   .body(data)
		 .when()
		   .patch(baseURI+"/booking/{id}")
		 .then()
		   .statusCode(equalTo(200))
		   .extract().response();
		   assertThat(res.jsonPath().get("firstname"), equalTo(firstname));   //updated firstname
		   assertThat(res.jsonPath().get("lastname"), equalTo(lastname));     //updated lastname 
		   assertThat(res.jsonPath().get("totalprice"), equalTo(bookingData.getTotalprice())); //old data for price and other fields
		   assertThat(res.jsonPath().get("depositpaid"), equalTo(bookingData.getDepositpaid()));
		   assertThat(res.jsonPath().get("bookingdates.checkin"), equalTo(bookingData.getBookingdates().getCheckin()));
		   assertThat(res.jsonPath().get("bookingdates.checkout"), equalTo(bookingData.getBookingdates().getCheckout()));
		   assertThat(res.jsonPath().get("additionalneeds"), equalTo(bookingData.getAdditionalneeds()));
	}
	
	
	/**
	 * This method provides you the booking detail related to a booking ID
	 * @param bookingId
	 */
	public void getBookingData(int bookingId) {
		Response res = RestAssured
		 .given()
		   .header("Accept","application/json")
		   .pathParam("id", bookingId)
		 .when()
		   .get(baseURI+"/booking/{id}")
		 .then()
		   .statusCode(equalTo(200))
		   .extract().response();
		try {
			bookingData = mapper.readValue(res.asString(), BookingData.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
	}
}
