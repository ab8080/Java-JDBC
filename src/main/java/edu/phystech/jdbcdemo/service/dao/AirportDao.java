package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.SimpleJdbcTemplate;
import edu.phystech.jdbcdemo.domain.Airport;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class AirportDao {
    private final SimpleJdbcTemplate source;

    private Airport createAirport(ResultSet resultSet) throws SQLException {
        return new Airport(resultSet.getString("airport_code"),
                resultSet.getString("airport_name"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone"));
    }

    public final void saveAirports(List<Airport> airports) throws SQLException {
        source.preparedStatement("insert into airports"
                + "(airport_code, airport_name, city, coordinates, timezone)"
                + " values (?, ?, ?, ?, ?)", insertAirport -> {
            for (Airport airport : airports) {
                insertAirport.setString(1, airport.getAirportCode());
                insertAirport.setString(2, airport.getAirportName());
                insertAirport.setString(3, airport.getCity());
                insertAirport.setString(4, airport.getCoordinates());
                insertAirport.setString(5, airport.getTimezone());
                insertAirport.execute();
            }
        });
    }

    public final List<Airport> getAirports() throws SQLException {
        return source.statement(stmt -> {
            List<Airport> result = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery("select * from airports");
            while (resultSet.next()) {
                result.add(createAirport(resultSet));
            }
            return result;
        });
    }

    public final List<String> getCitiesWithMultipleAirports() throws SQLException {
        return source.statement(stmt -> {
            String query = "select city, group_concat(airport_code) as airports \n"
                           + "from airports\n"
                           + "group by city\n"
                           + "having count(airport_code) > 1";
            ResultSet resultSet = stmt.executeQuery(query);
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                String airports = resultSet.getString("airports");
                result.add(city + " (" + airports + ")");
            }
            return result;
        });
    }

    public final List<String> getCitiesWithMostCanceledFlights() throws SQLException {
        return source.statement(stmt -> {
            String query = "select city, count(airport_code) as canceled_flights\n"
                    + " from airports\n"
                    + "join flights on airports.airport_code = flights.departure_airport\n"
                    + " where flights.status = 'Cancelled'\n"
                    + "group by city order by canceled_flights desc\n";
            ResultSet resultSet = stmt.executeQuery(query);
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                int canceledFlights = resultSet.getInt("canceled_flights");
                result.add(city + " (" + canceledFlights + ")");
            }
            return result;
        });
    }

    public final List<String> getFastestFlights() throws SQLException {
        return source.statement(stmt -> {
            String query = "select departure_city, arrival_city, avg(time_of_flight) as avg_time\n"
                    + "from (\n"
                    + "select departure_airport, arrival_airport, departure_city, arrival_city, actual_arrival"
                    + "- actual_departure as time_of_flight\n"
                    + "from flights\n"
                    + "join airports as departure_airports"
                    + "on departure_airports.airport_code = flights.departure_airport\n"
                    + "join airports as arrival_airports on arrival_airports.airport_code = flights.arrival_airport\n"
                    + "where arrival_time is not null\n"
                    + ") group by departure_city, arrival_city\n"
                    + "order by departure_city, avg_time";
            ResultSet resultSet = stmt.executeQuery(query);
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                String departureCity = resultSet.getString("departure_city");
                String arrivalCity = resultSet.getString("arrival_city");
                double avgTime = resultSet.getDouble("avg_time");
                result.add(departureCity + " -> " + arrivalCity + " (" + avgTime + ")");
            }
            return result;
        });
    }

    public final List<String> getNumberOfFlightCancellationsByMonth() throws SQLException {
        return source.statement(stmt -> {
            List<String> result = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery(
                    "select EXTRACT(MONTH FROM scheduled_departure) as month_number,"
                            + " count(status) as canceled_flights"
                            + " from flights where status = 'Cancelled'"
                            + " group by month_number order by month_number desc");
            while (resultSet.next()) {
                result.add(resultSet.getString("month_number") + " " + resultSet.getString("canceled_flights"));
            }
            return result;
        });
    }

    public final List<String> getNumberOfFlightsFromSVOVKODMEAirports() throws SQLException {
        return source.statement(stmt -> {
            List<String> result = new ArrayList<>();
            String query = "select EXTRACT(DOW FROM scheduled_departure) as day_of_week,"
                    + " count(status) as flights\n"
                    + " from flights where departure_airport in ('SVO', 'VKO', 'DME')\n"
                    + " group by day_of_week order by day_of_week desc";
            ResultSet resultSet = stmt.executeQuery(
                    query);
            while (resultSet.next()) {
                String dayOfWeek = resultSet.getString("day_of_week");
                switch (dayOfWeek) {
                    case "0":
                        dayOfWeek = "Sunday";
                        break;
                    case "1":
                        dayOfWeek = "Monday";
                        break;
                    case "2":
                        dayOfWeek = "Tuesday";
                        break;
                    case "3":
                        dayOfWeek = "Wednesday";
                        break;
                    case "4":
                        dayOfWeek = "Thursday";
                        break;
                    case "5":
                        dayOfWeek = "Friday";
                        break;
                    case "6":
                        dayOfWeek = "Saturday";
                        break;
                    case "7":
                        dayOfWeek = "Sunday";
                        break;
                    default:
                        dayOfWeek = "Unknown";
                }
                result.add(dayOfWeek + " " + resultSet.getString("flights"));
            }
            return result;
        });

    }

    public final void cancelFlightsByModel(String model) throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery(
                    "select aircraft_code from aircrafts where model = '" + model + "'");
            resultSet.next();
            String aircraftCode = resultSet.getString("aircraft_code");
            stmt.executeUpdate("update flights set status = 'Cancelled' where aircraft_code = '" + aircraftCode + "'");
            stmt.executeUpdate("delete from tickets where aircraft_code = '" + aircraftCode + "'");
        });
    }

    public final void addTicket(long flightId, String seat) throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery(
                    "select flight_id from flights where flight_id = " + flightId);
            if (!resultSet.next()) {
                throw new SQLException("Flight with id " + flightId + " does not exist");
            }
            resultSet = stmt.executeQuery(
                    "select seat_no from seats where seat_no = '" + seat + "'");
            if (!resultSet.next()) {
                throw new SQLException("Seat with number " + seat + " does not exist");
            }
            stmt.executeUpdate("insert into boarding_passes (flight_id, seat_no)"
                    + "values (" + flightId + ", '" + seat + "')");
        });
    }
}



