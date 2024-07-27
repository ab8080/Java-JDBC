# Homework Assignment #3

## Data Description

### Database Description
The database contains information on air travel within Russia in 2017. The main entity is a booking (bookings), which can include several passengers, each issued a separate ticket (tickets). A ticket includes one or more flights (ticket_flights). Each flight is associated with boarding passes (boarding_passes), which assign seats to passengers.

### Table Descriptions

#### Table: `aircrafts`
Identifies each aircraft model by a three-character code (aircraft_code). Contains the model name (model) and maximum flight range in kilometers (range).

#### Table: `airports`
Identifies airports by a three-character code (airport_code) and includes the airport name (airport_name), city (city), coordinates (coordinates), and timezone (timezone).

#### Table: `boarding_passes`
Contains information on boarding passes, identified by ticket number and flight ID. Includes boarding number (boarding_no) and seat number (seat_no).

#### Table: `bookings`
Identifies bookings by a six-character reference number (book_ref). Contains booking date (book_date) and total amount (total_amount).

#### Table: `flights`
Contains flight information, identified by a surrogate key (flight_id). Includes flight number (flight_no), scheduled and actual departure/arrival times, departure/arrival airports, status, and aircraft code.

#### Table: `seats`
Defines seating arrangements for each aircraft model. Includes seat number (seat_no) and fare conditions (fare_conditions).

#### Table: `ticket_flights`
Links tickets to flights. Includes ticket number (ticket_no), flight ID (flight_id), fare conditions (fare_conditions), and flight cost (amount).

#### Table: `tickets`
Identifies tickets by a 13-digit number (ticket_no). Includes booking reference (book_ref), passenger ID (passenger_id), passenger name (passenger_name), and contact data (contact_data).

## Tasks

### Task A: Data Loading
Implement code to download, parse, and load data into the database. Create the database and necessary tables, and populate them with data from CSV files.

### Task B: Database Operations
The program should execute the following queries:

1. List cities with multiple airports.
2. List cities with the highest number of canceled flights.
3. Find the shortest route from each departure city, sorted by duration.
4. Count flight cancellations by month.
5. Count flights to and from Moscow by day of the week and display two histograms on one chart.
6. Cancel all flights for a specified aircraft model and delete associated tickets.
7. Cancel all flights to and from Moscow within a specified date range and calculate the losses for airlines by day.
8. Add a new ticket, ensuring the flight and seat exist.

### Task C: Result Representation
Use Apache POI to generate Excel files for query results. Create charts with JFreeChart for tasks marked with "x".

### Task D: GitLab-CI (Optional for certain groups)
Set up a CI pipeline in GitLab to compile, test, and build the code, download data, create and populate the database, and execute queries. Use cache to handle database state between jobs.

## Evaluation Criteria

The assignment will be graded based on the completion of each task and the complexity of the implemented scenario. 

### Grading Breakdown

| Task                   | Points (Basic Scenario) | Points (Intermediate Scenario) |
|------------------------|-------------------------|--------------------------------|
| Task A                 | 2                       | 35                             |
| Task B                 | 93                      | 93                             |
| Task C                 | 25                      | 45                             |
| Task D                 | 30                      | 35                             |
| **Total**              | **150**                 | **208**                        |

Refer to the document for detailed point distribution for each subtask.

## Notes
- Each task should be implemented in a separate method.
- Each query should be part of a single transaction if it involves multiple queries.

For additional details and download links, please refer to the document sections.

Ссылка на задание на русском https://docs.google.com/document/d/1qzg_g7RVdl4_IImC1ino-207dZha8HuKO5D2Wsn_GPo/edit
