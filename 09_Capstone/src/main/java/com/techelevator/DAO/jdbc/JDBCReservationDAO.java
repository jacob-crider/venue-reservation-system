package com.techelevator.DAO.jdbc;

import com.techelevator.DAO.Reservation;
import com.techelevator.DAO.ReservationDAO;
import com.techelevator.DAO.Space;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCReservationDAO implements ReservationDAO {

    private JdbcTemplate jdbcTemplate;

    public JDBCReservationDAO (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Reservation addReservation(Space space, int numberOfAttendees, LocalDate startDate, LocalDate endDate, String reservedFor) {
        Reservation reservation = new Reservation();
        reservation.setSpace_id(space.getSpaceId());
        reservation.setNumberOfAttendees(numberOfAttendees);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setReservedFor(reservedFor);

        String sql = "INSERT INTO reservation (reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING reservation_id";
        Long reservationId = jdbcTemplate.queryForObject(sql, Long.class, space.getSpaceId(), numberOfAttendees, startDate, endDate, reservedFor);
        reservation.setReservation_id(reservationId);

        return reservation;
    }

    @Override
    public List<Reservation> listReservationsBySpaceIdWithScheduleConflict(Space space, LocalDate startingDateRequested, LocalDate endingDateRequested) {
            List<Reservation> reservations = new ArrayList<>();
            String sql = "SELECT * FROM reservation WHERE space_id = ? AND ((start_date <= ?  AND ? <= end_date) OR (start_date <= ? AND ? <= end_date))";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, space.getSpaceId(), startingDateRequested, startingDateRequested, endingDateRequested, endingDateRequested);
            while(results.next()) {
                Reservation reservation = new Reservation();
                reservation.setSpace_id(space.getSpaceId());
                reservations.add(reservation);
            }
            return reservations;
    }

}
