package edu.phystech.jdbcdemo.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class TableParser {
    static final int MAGIC_EIGHT = 8;
    static final int MAGIC_NINE = 9;
    static final int MAGIC_TEN = 10;
    private BufferedReader getCSV(String tableName) throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/" + tableName);
        return new BufferedReader(new InputStreamReader(url.openStream()));
    }
    public final void parseTable(String tableName, edu.phystech.jdbcdemo.SimpleJdbcTemplate source)
            throws IOException, SQLException {
        BufferedReader br = getCSV(tableName);
        Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String line;
        switch (tableName) {
            case "aircrafts.csv":
                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into aircrafts(aircraft_code, model, range) values (?, ?, ?)",
                            stmt -> {
                                String[] values = pattern.split(currentLine);
                                stmt.setString(1, values[0]);
                                stmt.setString(2, values[1].replaceAll("\"", ""));
                                stmt.setInt(3, Integer.parseInt(values[2]));
                                stmt.execute();
                            });
                }
                break;
            case "airports.csv":
                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into airports"
                            + "(airport_code, airport_name, city, coordinates, timezone)"
                            + " values (?, ?, ?, ?, ?)", stmt -> {
                        String[] values = pattern.split(currentLine);
                        stmt.setString(1, values[0]);
                        stmt.setString(2, values[1].replaceAll("\"", ""));
                        stmt.setString(3, values[2].replaceAll("\"", ""));
                        List<String> points = Arrays.asList(values[3].
                                        replaceAll("[^0-9.]+", " ").
                                        trim().split(" "));
                        String point = "POINT(" + points.get(0) + " " + points.get(1) + ")";
                        stmt.setString(4, point);
                        stmt.setString(5, values[4]);
                        stmt.execute();
                    });
                }
                break;
            case "boarding_passes.csv":
                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into boarding_passes"
                            + "(ticket_no, flight_id, boarding_no, seat_no) values (?, ?, ?, ?)", stmt -> {
                        String[] values = pattern.split(currentLine);
                        stmt.setString(1, values[0]);
                        stmt.setInt(2, Integer.parseInt(values[1]));
                        stmt.setInt(3, Integer.parseInt(values[2]));
                        stmt.setString(4, values[3]);
                        stmt.execute();
                    });
                }
                break;
            case "bookings.csv":
                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into bookings"
                            + "(book_ref, book_date, total_amount) values (?, ?, ?)", stmt -> {
                        String[] values = pattern.split(currentLine);
                        stmt.setString(1, values[0]);
                        stmt.setString(2, values[1]);
                        stmt.setDouble(3, Double.parseDouble(values[2]));
                        stmt.execute();
                    });
                }
                break;
            case "flights.csv":


                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into flights"
                            + "(flight_id, flight_no, scheduled_departure, scheduled_arrival, departure_airport,"
                            + " arrival_airport, status, aircraft_code, actual_departure, actual_arrival)"
                            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", stmt -> {
                        String[] values = pattern.split(currentLine);
                        stmt.setInt(1, Integer.parseInt(values[0]));
                        stmt.setString(2, values[1]);
                        stmt.setString(3, values[2]);
                        stmt.setString(4, values[3]);
                        stmt.setString(5, values[4]);
                        stmt.setString(6, values[5]);
                        stmt.setString(7, values[6]);
                        stmt.setString(MAGIC_EIGHT, values[7]);
                        if (values.length >= MAGIC_NINE) {
                            stmt.setString(MAGIC_NINE, values[MAGIC_EIGHT]);
                        } else {
                            stmt.setString(MAGIC_NINE, null);
                        }
                        if (values.length >= MAGIC_TEN) {
                            stmt.setString(MAGIC_TEN, values[MAGIC_NINE]);
                        } else {
                            stmt.setString(MAGIC_TEN, null);
                        }
                        stmt.execute();
                    });
                }
                break;
            case "seats.csv":
                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into seats"
                            + "(aircraft_code, seat_no, fare_conditions) values (?, ?, ?)", stmt -> {
                        String[] values = pattern.split(currentLine);
                        stmt.setString(1, values[0]);
                        stmt.setString(2, values[1]);
                        stmt.setString(3, values[2]);
                        stmt.execute();
                    });
                }
                break;
            case "ticket_flights.csv":
                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into ticket_flights"
                            + "(ticket_no, flight_id, fare_conditions, amount) values (?, ?, ?, ?)", stmt -> {
                        String[] values = pattern.split(currentLine);
                        stmt.setString(1, values[0]);
                        stmt.setInt(2, Integer.parseInt(values[1]));
                        stmt.setString(3, values[2]);
                        stmt.setDouble(4, Double.parseDouble(values[3]));
                        stmt.execute();
                    });
                }
                break;
            case "tickets.csv":
                while ((line = br.readLine()) != null) {
                    String currentLine = line;
                    source.preparedStatement("insert into tickets"
                                    + "(ticket_no, book_ref, passenger_id, passenger_name, contact_data)"
                                    + "values (?, ?, ?, ?, ?)",
                            stmt -> {
                                String[] values = pattern.split(currentLine);
                                stmt.setString(1, values[0]);
                                stmt.setString(2, values[1]);
                                stmt.setString(3, values[2]);
                                stmt.setString(4, values[3]);
                                stmt.setString(5, values[4].replaceAll("\"", ""));
                                stmt.execute();
                            });
                }
                break;

        }
    }
}
