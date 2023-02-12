package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class Flight {
    private final int flightId;
    private final String flightNo;
    private final String scheduledDeparture;
    private final String scheduledArrival;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String status;
    private final String aircraftCode;
    private final String actualDeparture;
    private final String actualArrival;
}
