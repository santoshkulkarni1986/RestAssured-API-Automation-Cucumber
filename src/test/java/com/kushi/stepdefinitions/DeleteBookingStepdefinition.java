package com.kushi.stepdefinitions;

import com.kushi.utility.TestContextUtility;

import io.cucumber.java.en.When;

public class DeleteBookingStepdefinition {
	private TestContextUtility context;

	public DeleteBookingStepdefinition(TestContextUtility context) {
		this.context = context;
	}

	@When("user makes a request to delete booking with basic auth {string} & {string}")
	public void userMakesARequestToDeleteBookingWithBasicAuth(String username, String password) {
		context.response = context.requestSetup()
				.auth().preemptive().basic(username, password)
				.pathParam("bookingID", context.getSession().get("bookingID"))
				.when().delete(context.getSession().get("endpoint")+"/{bookingID}");
	}
}
