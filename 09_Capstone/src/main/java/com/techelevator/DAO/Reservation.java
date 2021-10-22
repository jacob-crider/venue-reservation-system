package com.techelevator.DAO;

import java.time.LocalDate;

public class Reservation {

    private long reservation_id;
    private long space_id;
    private int numberOfAttendees;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reservedFor;

    public long getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(long reservation_id) {
        this.reservation_id = reservation_id;
    }

    public long getSpace_id() {
        return space_id;
    }

    public void setSpace_id(long space_id) {
        this.space_id = space_id;
    }

    public int getNumberOfAttendees() {
        return numberOfAttendees;
    }

    public void setNumberOfAttendees(int numberOfAttendees) {
        this.numberOfAttendees = numberOfAttendees;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReservedFor() {
        return reservedFor;
    }

    public void setReservedFor(String reservedFor) {
        this.reservedFor = reservedFor;
    }
}
