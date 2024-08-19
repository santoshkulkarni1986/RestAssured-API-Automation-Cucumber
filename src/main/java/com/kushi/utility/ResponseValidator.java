package com.kushi.utility;

import com.kushi.payloads.Booking;
import com.kushi.payloads.BookingResponse;

import static org.junit.Assert.assertNotNull;

public class ResponseValidator {
	
	private BookingResponse bookingResponse;

	public static void validateBookingResponse(Booking expectedBooking, BookingResponse actualResponse) {
        Booking actualBooking = actualResponse.getBooking();

        assertNotNull("Booking Name Mismatch", actualBooking.getFirstname());
        assertNotNull("Last Name Miss Match", actualBooking.getLastname());
        assertNotNull("Total Price Miss Match", actualBooking.getTotalprice());
        assertNotNull("Deposited paid Miss Match", actualBooking.getDepositpaid());
        assertNotNull("Checkin Details Miss match", actualBooking.getBookingdates().getCheckin());
        assertNotNull("Checkout Details Miss match" ,actualBooking.getBookingdates().getCheckout());
        assertNotNull("Additional Details Missmatch", actualBooking.getAdditionalneeds());
    }

	
}
