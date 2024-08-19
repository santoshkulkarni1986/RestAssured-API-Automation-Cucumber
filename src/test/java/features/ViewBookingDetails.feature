#@RestFullAPI, @DeleteBooking
#Feature: List of scenarios of restful-booker booking details
@BookerAPI @updateBooking
Feature: List of scenarios of restful-booker Viewing the Booking details, By ID,By IDS and By Dates
 
  @viewAllBookingByUsingIDS
  Scenario: To view all the booking IDs
    Given user has access to endpoint "/booking"
    When user makes a request to view booking IDs
    Then user should get the response code 200
    And user should see all the booking IDs

  @viewBookingDetailsByID
  Scenario: To view booking details
    Given user has access to endpoint "/booking"
    When user makes a request to view booking IDs
    And user makes a request to view details of a booking ID
    Then user should get the response code 200
    And user validates the response with JSON schema "bookingDetailsSchema.json"

  @viewByBookingDetailsByDates
  Scenario Outline: To view all the booking IDs by booking dates
    Given user has access to endpoint "/booking"
    When user makes a request to view booking IDs from "<checkin>" to "<checkout>"
    Then user should get the response code 200
    And user should see all the booking IDs

    Examples: 
      | checkin    | checkout   |
      | 2024-05-15 | 2024-05-10 |
      | 2024-06-01 | 2024-07-10 |

  @viewBookingDetailsByName
  Scenario: To view all the booking IDs by booking names
    Given user has access to endpoint "/booking"
    When user makes a request to view booking IDs
    Then user should see all the booking IDs
    And user makes a request to view details of a booking ID
    And user makes a request to view all the booking IDs of that user name
    And user should get the response code 200
    And user should see all the booking IDs

  @healthCheck
  Scenario: To confirm whether the API is up and running
    Given user has access to endpoint "/ping"
    When user makes a request to check the health of booking service
    Then user should get the response code 201
