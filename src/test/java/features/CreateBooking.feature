#@RestFullAPI
#Feature: List of scenarios of Create Booking by using Data Table, Json File, Excel File and Using Pojo Classes
@BookerAPI @createBookingFromExcelAndJsonAndDataTableAndPojoClasses
Feature: List of scenarios of Create Booking by using Data Table, Json File, Excel File and Using Pojo Classes


  @createBookingUsingDataTable
  Scenario Outline: To create new booking using cucumber Data Table
    Given user has access to endpoint "/booking"
    When user creates a booking
      | firstname   | lastname   | totalprice   | depositpaid   | checkin   | checkout   | additionalneeds   |
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |
    Then user should get the response code 200
    And user validates the response with JSON schema "createBookingSchema.json"

    Examples: 
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Alice     | Smith    |       1500 | true        | 2023-10-01 | 2023-10-10 | Lunch           |
      | Bob       | Johnson  |       2000 | false       | 2023-11-01 | 2023-11-15 | Dinner          |

  @createBookingByUsingExcel
  Scenario Outline: To create new booking using Excel data
    Given user has access to endpoint "/booking"
    When user creates a booking using data "<dataKey>" from Excel
    Then user should get the response code 200
    And user validates the response with JSON schema from Excel

    Examples: 
      | dataKey        |
      | createBooking1 |
      | createBooking2 |

  @createBookingByUsingJSONFile
  Scenario Outline: To create new booking using JSON data
    Given user has access to endpoint "/booking"
    When user creates a booking using data "<dataKey>" from JSON file "<JSONFile>"
    Then user should get the response code 200
    And user validates the response with JSON schema "createBookingSchema.json"

    Examples: 
      | dataKey        | JSONFile         |
      | createBooking1 | bookingBody.json |
      | createBooking2 | bookingBody.json |

  @createBookingWithPojoClasses
  Scenario Outline: To create new booking using POJO classes
    Given user has access to endpoint "/booking"
    When user creates a booking with the following details
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |
    Then user should get the response code 200
    And user validates the response with POJO classes

    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Carol     | Davis    |       1300 | true        | 2023-12-01 | 2023-12-15 | Vegan            |
      | Dave      | Wilson   |       1700 | false       | 2024-01-01 | 2024-01-10 | Gluten-Free      |
