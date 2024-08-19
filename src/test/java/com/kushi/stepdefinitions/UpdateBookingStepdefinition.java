package com.kushi.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.kushi.payloads.Booking;
import com.kushi.utility.ExcelUtility;
import com.kushi.utility.JsonUtility;
import com.kushi.utility.ResponseHandlerUtility;
import com.kushi.utility.TestContext;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;

public class UpdateBookingStepdefinition {
	private TestContext context;
	private static final Logger LOG = LogManager.getLogger(UpdateBookingStepdefinition.class);

	public UpdateBookingStepdefinition(TestContext context) {
		this.context = context;
	}

	@When("user creates a auth token with credential {string} & {string}")
	public void userCreatesAAuthTokenWithCredential(String username, String password) {
		JSONObject credentials = new JSONObject();
		credentials.put("username", username);
		credentials.put("password", password);
		context.response = context.requestSetup().body(credentials.toString()).when()
				.post(context.session.get("endpoint").toString());
		String token = context.response.path("token");
		LOG.info("Auth Token: " + token);
		context.session.put("token", "token=" + token);
	}

	@When("user updates the details of a booking")
	public void userUpdatesABooking(DataTable dataTable) {
		Map<String, String> bookingData = dataTable.asMaps().get(0);
		JSONObject bookingBody = new JSONObject();
		bookingBody.put("firstname", bookingData.get("firstname"));
		bookingBody.put("lastname", bookingData.get("lastname"));
		bookingBody.put("totalprice", bookingData.get("totalprice"));
		bookingBody.put("depositpaid",(bookingData.get("depositpaid")));
		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", (bookingData.get("checkin")));
		bookingDates.put("checkout", (bookingData.get("checkout")));
		bookingBody.put("bookingdates", bookingDates);
		bookingBody.put("additionalneeds", bookingData.get("additionalneeds"));

		context.response = context.requestSetup().header("Cookie", context.session.get("token").toString())
				.pathParam("bookingID", context.session.get("bookingID")).body(bookingBody.toString()).when()
				.put(context.session.get("endpoint") + "/{bookingID}");

		Booking bookingDetailsDTO = ResponseHandlerUtility.deserializeResponse(context.response,Booking.class);
		assertNotNull("Booking not created", bookingDetailsDTO);
	}

	@When("user updates the booking details using data {string} from Excel")
	public void userUpdatesTheBookingDetailsUsingDataFromExcel(String dataKey) throws Exception {
		Map<String, String> excelDataMap = ExcelUtility.getData(dataKey);
		context.response = context.requestSetup().header("Cookie", context.session.get("token").toString())
				.pathParam("bookingID", context.session.get("bookingID")).body(excelDataMap.get("requestBody")).when()
				.put(context.session.get("endpoint") + "/{bookingID}");

		Booking bookingDetails = ResponseHandlerUtility.deserializeResponse(context.response,Booking.class);
		assertNotNull("Booking not created", bookingDetails);
		context.session.put("excelDataMap", excelDataMap);
	}

	@When("user updates the booking details using data {string} from JSON file {string}")
	public void userUpdatesTheBookingDetailsUsingDataFromJSONFile(String dataKey, String JSONFile) {
		context.response = context.requestSetup().header("Cookie", context.session.get("token").toString())
				.pathParam("bookingID", context.session.get("bookingID"))
				.body(JsonUtility.getRequestBody(JSONFile, dataKey)).when()
				.put(context.session.get("endpoint") + "/{bookingID}");

		Booking bookingDetails = ResponseHandlerUtility.deserializeResponse(context.response,Booking.class);
		assertNotNull("Booking not created", bookingDetails);
	}

	@When("user makes a request to update first name {string} & Last name {string}")
	public void userMakesARequestToUpdateFirstNameLastName(String firstName, String lastName) {
		JSONObject body = new JSONObject();
		body.put("firstname", firstName);
		body.put("lastname", lastName);

		context.response = context.requestSetup().header("Cookie", context.session.get("token").toString())
				.pathParam("bookingID", context.session.get("bookingID")).body(body.toString()).when()
				.patch(context.session.get("endpoint") + "/{bookingID}");

		Booking bookingDetails = ResponseHandlerUtility.deserializeResponse(context.response,Booking.class);
		assertNotNull("Booking not created", bookingDetails);
		assertEquals("First Name did not match", firstName, bookingDetails.getFirstname());
		assertEquals("Last Name did not match", lastName, bookingDetails.getLastname());
	}
}
