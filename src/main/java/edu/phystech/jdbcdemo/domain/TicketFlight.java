package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class TicketFlight {
    private final String ticketNo;
    private final int flightId;
    private final String fareConditions;
    private final double amount;
}
