#@RestFullAPI, @DeleteBooking
#Feature: List of scenarios of updating Booking by using Data Table, Excel File, JsonFile and Parital Update.
@BookerAPI @updateBookingByUsingDataTableAndExcelAndJsonAndPartialUpdate
Feature: List of scenarios of updating Booking by using Data Table, Excel File, JsonFile and Parital Update.

  Background: create an auth token
    Given user has access to endpoint "/auth"
    When user creates a auth token with credential "admin" & "password123"
    Then user should get the response code 200

  @updateBookingByUsingDataTable
  Scenario Outline: To update a booking using cucumber Data Table
    Given user has access to endpoint "/booking"
    When user makes a request to view booking IDs
    And user updates the details of a booking
      | firstname   | lastname   | totalprice   | depositpaid   | checkin   | checkout   | additionalneeds   |
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |
    Then user should get the response code 200
    And user validates the response with JSON schema "bookingDetailsSchema.json"

    Examples: 
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Alice     | Rambo    |      10000 | true        | 2024-05-15 | 2024-05-10 | Breakfast       |
      | Bob       | Balboa   |       2006 | false       | 2024-06-01 | 2024-07-10 | Dinner          |

  @updateBookingByUsingFromExcel
  Scenario Outline: To create and update a new booking using Excel data
    Given user has access to endpoint "/booking"
    And user creates a booking using data "<createKey>" from Excel
    When user updates the booking details using data "<updateKey>" from Excel
    Then user should get the response code 200
    And user validates the response with JSON schema from Excel

    Examples: 
      | createKey      | updateKey      |
      | createBooking1 | updateBooking1 |
      | createBooking2 | updateBooking2 |

  @updateBookingByUsingJSON
  Scenario Outline: To update a booking using JSON data
    Given user has access to endpoint "/booking"
    When user makes a request to view booking IDs
    And user updates the booking details using data "<dataKey>" from JSON file "<JSONFile>"
    Then user should get the response code 200
    And user validates the response with JSON schema "bookingDetailsSchema.json"

    Examples: 
      | dataKey        | JSONFile         |
      | updateBooking1 | bookingBody.json |
      | updateBooking2 | bookingBody.json |

  @partialUpdatingTheBookingDetails
  Scenario: To partially update a booking
    Given user has access to endpoint "/booking"
    When user makes a request to view booking IDs
    And user makes a request to update first name "Santosh" & Last name "Kulkarni"
    Then user should get the response code 200
    And user validates the response with JSON schema "bookingDetailsSchema.json"
