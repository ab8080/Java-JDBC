package edu.phystech.jdbcdemo;

import edu.phystech.jdbcdemo.domain.TableParser;
import edu.phystech.jdbcdemo.service.dao.AirportDao;
import edu.phystech.jdbcdemo.service.db.DataBaseInit;
import org.h2.jdbcx.JdbcConnectionPool;
import java.util.List;

import java.io.IOException;
import java.sql.SQLException;

public final class Hello {
    private Hello() { }

    public static void main(String[] args) throws SQLException, IOException {
        // Idea подсвечивает красным SimpleJdbcTemplate, но style checker сказал, что импорт был избыточен, все работает
        SimpleJdbcTemplate template = new SimpleJdbcTemplate(JdbcConnectionPool.create(
                "jdbc:h2:mem:database;DB_CLOSE_DELAY=-1", "", ""));
        new DataBaseInit(template).create();
        TableParser parser = new TableParser();
        parser.parseTable("airports.csv", template);
        parser.parseTable("aircrafts.csv", template);
        parser.parseTable("bookings.csv", template);
        parser.parseTable("tickets.csv", template);
        parser.parseTable("flights.csv", template);
        parser.parseTable("ticket_flights.csv", template);
        parser.parseTable("boarding_passes.csv", template);
        parser.parseTable("seats.csv", template);
        List<String> res;
        AirportDao a = new AirportDao(template);

        res = a.getCitiesWithMultipleAirports(); // task1
        // res = a.getCitiesWithMostCanceledFlights(); // task2

        // res = a.getFastestFlights(); // task3
        //res = a.getNumberOfFlightCancellationsByMonth(); // task4
        //res = a.getNumberOfFlightsFromSVOVKODMEAirports(); // task5
        // a.cancelFlightsByModel("Boeing 737-300"); // task6

        for (String s : res) {
            System.out.println(s);
        }

        /* task8
        try {
            a.addTicket(1234, "2X");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        */
    }

}
