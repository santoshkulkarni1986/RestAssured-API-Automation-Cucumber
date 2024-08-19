package com.kushi.payloads;

public class BookingResponse {
    private int bookingid;
    private Booking booking;
    private Booking bookingdetails;


    // Getters and Setters
    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
    public Booking getBookingdetails() {
		return bookingdetails;
	}

	public void setBookingdetails(Booking bookingdetails) {
		this.bookingdetails = bookingdetails;
	}
    
}
