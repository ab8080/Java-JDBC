drop table if exists aircrafts cascade;
create table aircrafts
(
    aircraft_code char(3) not null,
    model         json   not null,
    range         integer not null,
    primary key (aircraft_code)
);

drop table if exists airports cascade;
create table airports
(
    airport_code char(3) not null,
    airport_name json   not null,
    city         json   not null,
    coordinates  geometry(point)   not null,
    timezone     text    not null,
    primary key (airport_code)
);

drop table if exists bookings cascade;
create table bookings
(
    book_ref     char(6)                  not null,
    book_date    timestamp with time zone not null,
    total_amount numeric(10, 2)           not null,
    primary key (book_ref)
);

drop table if exists tickets cascade;
create table tickets
(
    ticket_no      char(13)    not null,
    book_ref       char(6)     not null,
    passenger_id   varchar(20) not null,
    passenger_name text        not null,
    contact_data   json,
    primary key (ticket_no),
    foreign key (book_ref) references bookings
);

drop table if exists flights cascade;
create table flights
(
    flight_id           serial                   not null,
    flight_no           char(6)                  not null,
    scheduled_departure timestamp with time zone not null,
    scheduled_arrival   timestamp with time zone not null,
    departure_airport   char(3)                  not null,
    arrival_airport     char(3)                  not null,
    status              varchar(20)              not null,
    aircraft_code       char(3)                  not null,
    actual_departure    timestamp with time zone,
    actual_arrival      timestamp with time zone,
    primary key (flight_id),
    foreign key (aircraft_code) references aircrafts,
    foreign key (departure_airport) references airports,
    foreign key (arrival_airport) references airports
);

drop table if exists ticket_flights cascade;
create table ticket_flights
(
    ticket_no       char(13)       not null,
    flight_id       integer        not null,
    fare_conditions varchar(10)    not null,
    amount          numeric(10, 2) not null,
    primary key (ticket_no, flight_id),
    foreign key (ticket_no) references tickets,
    foreign key (flight_id) references flights
);

drop table if exists boarding_passes cascade;
create table boarding_passes
(
    ticket_no   char(13)   not null,
    flight_id   integer    not null,
    boarding_no integer    not null,
    seat_no     varchar(4) not null,
    foreign key (ticket_no, flight_id) references ticket_flights
);

drop table if exists seats cascade;
create table seats
(
    aircraft_code   char(3)     not null,
    seat_no         varchar(4)  not null,
    fare_conditions varchar(10) not null,
    foreign key (aircraft_code) references aircrafts
);
