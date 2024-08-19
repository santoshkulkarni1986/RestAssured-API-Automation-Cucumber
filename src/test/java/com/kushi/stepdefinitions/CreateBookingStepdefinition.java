package com.kushi.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
//import org.jvnet.staxex.StAxSOAPBody.Payload;

import com.kushi.utility.Constants;
import com.kushi.utility.ExcelUtility;
import com.kushi.utility.JsonUtility;
import com.kushi.payloads.*;
import com.kushi.utility.ResponseHandlerUtility;
import com.kushi.utility.ResponseValidator;
import com.kushi.utility.SchemaValidator;
import com.kushi.utility.TestContext;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;

public class CreateBookingStepdefinition {
    private TestContext context;
    private Booking expectedBooking;
    private static final Logger LOG = LogManager.getLogger(CreateBookingStepdefinition.class);

    public CreateBookingStepdefinition(TestContext context) {
        this.context = context;
    }

    @Given("user has access to endpoint {string}")
    public void userHasAccessToEndpoint(String endpoint) {
        context.session.put("endpoint", endpoint);
    }

    @When("user creates a booking")
    public void userCreatesABooking(DataTable dataTable) {
    	Map<String,String> bookingData = dataTable.asMaps().get(0);
		JSONObject bookingBody = new JSONObject();
		bookingBody.put("firstname", bookingData.get("firstname"));
		bookingBody.put("lastname", bookingData.get("lastname"));
		bookingBody.put("totalprice", Integer.valueOf(bookingData.get("totalprice")));
		bookingBody.put("depositpaid", Boolean.valueOf(bookingData.get("depositpaid")));
		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", (bookingData.get("checkin")));
		bookingDates.put("checkout", (bookingData.get("checkout")));
		bookingBody.put("bookingdates", bookingDates);
		bookingBody.put("additionalneeds", bookingData.get("additionalneeds"));

		context.response = context.requestSetup().body(bookingBody.toString())
				.when().post(context.session.get("endpoint").toString());

        BookingResponse bookingDTO = ResponseHandlerUtility.deserializeResponse(context.response,BookingResponse.class);
		assertNotNull("Booking not created", bookingDTO);
		LOG.info("Newly created booking ID: "+bookingDTO.getBookingid());
		context.session.put("bookingID", bookingDTO.getBookingid());
		validateBookingData(new JSONObject(bookingData), bookingDTO);
    }

    @When("user creates a booking using data {string} from Excel")
    public void userCreatesABookingUsingDataFromExcel(String dataKey) throws Exception {
        Map<String, String> excelDataMap = ExcelUtility.getData(dataKey);
        context.response = context.requestSetup().body(excelDataMap.get("requestBody"))
                .when().post(context.session.get("endpoint").toString());

        BookingResponse bookingDTO = ResponseHandlerUtility.deserializeResponse(context.response, BookingResponse.class);
        assertNotNull("Booking not created", bookingDTO);
        LOG.info("Newly created booking ID: " + bookingDTO.getBookingid());
        context.session.put("bookingID", bookingDTO.getBookingid());
        validateBookingData(new JSONObject(excelDataMap.get("responseBody")), bookingDTO);
        context.session.put("excelDataMap", excelDataMap);
    }

    @Then("user validates the response with JSON schema from Excel")
    public void userValidatesTheResponseWithJSONSchemaFromExcel() {
    	context.response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(((Map<String,String>) context.session.get("excelDataMap")).get("responseSchema")));
		LOG.info("Successfully Validated schema from Excel");
    }

    @When("user creates a booking using data {string} from JSON file {string}")
    public void userCreatesABookingUsingDataFromJSONFile(String dataKey, String JSONFile) {
        context.response = context.requestSetup().body(JsonUtility.getRequestBody(JSONFile, dataKey))
                .when().post(context.session.get("endpoint").toString());

        BookingResponse bookingDetails = ResponseHandlerUtility.deserializeResponse(context.response, BookingResponse.class);
        assertNotNull("Booking not created", bookingDetails);
        LOG.info("Newly created booking ID: " + bookingDetails.getBookingid());
        context.session.put("bookingID", bookingDetails.getBookingid());
    }

    @When("user creates a booking with the following details")
    public void userCreatesABookingWithTheFollowingDetails(DataTable dataTable) {
    	 Map<String, String> bookingData = dataTable.asMaps().get(0);

         // Create BookingDates object
         BookingDates bookingDates = new BookingDates();
         bookingDates.setCheckin(bookingData.get("checkin"));
         bookingDates.setCheckout(bookingData.get("checkout"));

         // Create Booking object
         Booking bookingDetails = new Booking();
         bookingDetails.setFirstname(bookingData.get("firstname"));
         bookingDetails.setLastname(bookingData.get("lastname"));
         bookingDetails.setTotalprice(bookingData.get("totalprice"));  // Use String as POJO expects String
         bookingDetails.setDepositpaid(bookingData.get("depositpaid"));  // Use String as POJO expects String
         bookingDetails.setBookingdates(bookingDates);
         bookingDetails.setAdditionalneeds(bookingData.get("additionalneeds"));

         // Send the request
         context.response = context.requestSetup().body(bookingDetails)
                 .when().post(context.session.get("endpoint").toString());

         BookingResponse responseBookingDetails = ResponseHandlerUtility.deserializeResponse(context.response, BookingResponse.class);
         assertNotNull("Booking not created", responseBookingDetails);
         LOG.info("Newly created booking ID: " + responseBookingDetails.getBookingid());
         context.session.put("bookingID", responseBookingDetails.getBookingid());
    }

    @Then("user validates the response with POJO classes")
    public void userValidatesTheResponseWithPOJOClasses() {
    	BookingResponse actualResponse = context.response.as(BookingResponse.class);
        ResponseValidator.validateBookingResponse(expectedBooking, actualResponse);
    	
        }
    @Then("user should get the response code {int}")
    public void userShouldGetTheResponseCode(int expectedStatusCode) {
        int actualStatusCode = context.response.getStatusCode();
        context.validateStatusCode(expectedStatusCode);
        assertEquals("Expected status code was " + expectedStatusCode + " but got " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
    }

    @Then("user validates the response with JSON schema {string}")
    public void userValidatesTheResponseWithJSONSchema(String schemaFileName) {
    	SchemaValidator.validateResponseWithSchema(context.response, schemaFileName);
        LOG.info("Successfully Validated schema from " + schemaFileName);
		LOG.info("Successfully Validated schema from "+schemaFileName);
    }

    private void validateBookingData(JSONObject bookingData, BookingResponse responseBookingDetails) {
        LOG.info(bookingData);
        assertNotNull("Booking ID missing", responseBookingDetails.getBookingid());
        assertEquals("First Name did not match", bookingData.get("firstname"), responseBookingDetails.getBooking().getFirstname());
        assertEquals("Last Name did not match", bookingData.get("lastname"), responseBookingDetails.getBooking().getLastname());
        assertEquals("Total Price did not match", bookingData.get("totalprice"), responseBookingDetails.getBooking().getTotalprice());
        assertEquals("Deposit Paid did not match", bookingData.get("depositpaid"), responseBookingDetails.getBooking().getDepositpaid());
        assertEquals("Additional Needs did not match", bookingData.get("additionalneeds"), responseBookingDetails.getBooking().getAdditionalneeds());
        assertEquals("Check in Date did not match", bookingData.get("checkin"), responseBookingDetails.getBooking().getBookingdates().getCheckin());
        assertEquals("Check out Date did not match", bookingData.get("checkout"), responseBookingDetails.getBooking().getBookingdates().getCheckout());
    }
}
