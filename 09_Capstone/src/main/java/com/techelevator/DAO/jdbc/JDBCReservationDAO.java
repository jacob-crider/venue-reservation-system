package com.techelevator.DAO.jdbc;

import com.techelevator.DAO.Reservation;
import com.techelevator.DAO.ReservationDAO;
import com.techelevator.DAO.Space;
import jdk.vm.ci.meta.Local;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;

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

}
