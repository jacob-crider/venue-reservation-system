package com.techelevator.DAO;

import java.time.LocalDate;

public interface ReservationDAO {

    Reservation addReservation(Space space, int numberOfAttendees, LocalDate startDate, LocalDate endDate, String reservedFor);
}
