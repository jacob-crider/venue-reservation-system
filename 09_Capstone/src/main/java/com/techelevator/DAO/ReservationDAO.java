package com.techelevator.DAO;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

    Reservation addReservation(Space space, int numberOfAttendees, LocalDate startDate, LocalDate endDate, String reservedFor);
    public List<Reservation> listReservationsBySpaceIdWithScheduleConflict(Space space, LocalDate startingDateRequested, LocalDate endingDateRequested);
}
