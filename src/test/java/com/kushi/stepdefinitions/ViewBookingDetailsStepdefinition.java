package com.kushi.stepdefinitions;

import static org.junit.Assert.assertNotNull;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.kushi.payloads.Booking;
import com.kushi.payloads.BookingResponse;
import com.kushi.utility.ResponseHandlerUtility;
import com.kushi.utility.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ViewBookingDetailsStepdefinition {
	private TestContext context;
	private static final Logger LOG = LogManager.getLogger(ViewBookingDetailsStepdefinition.class);

	public ViewBookingDetailsStepdefinition(TestContext context) {
		this.context = context;
	}



	@When("user makes a request to view booking IDs")
	public void userMakesARequestToViewBookingIDs() {
		context.response = context.requestSetup().when().get(context.session.get("endpoint").toString());
		int bookingID = context.response.getBody().jsonPath().getInt("[0].bookingid");
		LOG.info("Booking ID: " + bookingID);
		assertNotNull("Booking ID not found!", bookingID);
		context.session.put("bookingID", bookingID);
	}

	@Then("user should see all the booking IDs")
	public void userShouldSeeAllTheBookingIDS() {
		BookingResponse[] bookingIDs = ResponseHandlerUtility.deserializeResponse(context.response, BookingResponse[].class);
		assertNotNull("Booking ID not found!!", bookingIDs);
	}

	@Then("user makes a request to view details of a booking ID")
	public void userMakesARequestToViewDetailsOfBookingID() {
		LOG.info("Session BookingID: " + context.session.get("bookingID"));
		context.response = context.requestSetup().pathParam("bookingID", context.session.get("bookingID")).when()
				.get(context.session.get("endpoint") + "/{bookingID}");
		Booking bookingDetails = ResponseHandlerUtility.deserializeResponse(context.response,Booking.class);
		assertNotNull("Booking Details not found!!", bookingDetails);
		context.session.put("firstname", bookingDetails.getFirstname());
		context.session.put("lastname", bookingDetails.getLastname());
	}

	@Given("user makes a request to view booking IDs from {string} to {string}")
	public void userMakesARequestToViewBookingFromTo(String checkin, String checkout) {
		context.response = context.requestSetup().queryParams("checkin", checkin, "checkout", checkout).when()
				.get(context.session.get("endpoint").toString());
	}

	@Then("user makes a request to view all the booking IDs of that user name")
	public void userMakesARequestToViewBookingIDByUserName() {
		LOG.info("Session firstname: " + context.session.get("firstname"));
		LOG.info("Session lastname: " + context.session.get("lastname"));
		context.response = context.requestSetup()
				.queryParams("firstname", context.session.get("firstname"), "lastname", context.session.get("lastname"))
				.when().get(context.session.get("endpoint").toString());
		BookingResponse[] bookingIDs = ResponseHandlerUtility.deserializeResponse(context.response, BookingResponse[].class);
		assertNotNull("Booking ID not found!!", bookingIDs);
	}


	@When("user makes a request to check the health of booking service")
	public void userMakesARequestToCheckTheHealthOfBookingService() {
		context.response = context.requestSetup().get(context.session.get("endpoint").toString());
	}
}
