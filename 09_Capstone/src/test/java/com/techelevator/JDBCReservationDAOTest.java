package com.techelevator;

import com.techelevator.DAO.Reservation;
import com.techelevator.DAO.Space;
import com.techelevator.DAO.Venue;
import com.techelevator.DAO.jdbc.JDBCReservationDAO;
import com.techelevator.DAO.jdbc.JDBCSpaceDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class JDBCReservationDAOTest extends DAOIntegrationTest {

    private JDBCSpaceDAO jdbcSpaceDAO;
    private JdbcTemplate jdbcTemplate;
    private JDBCReservationDAO jdbcReservationDAO;

    @Before
    public void setup() {
        jdbcSpaceDAO = new JDBCSpaceDAO(getDataSource());
        jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcReservationDAO = new JDBCReservationDAO(getDataSource());
    }

    @Test
    public void add_reservation_test() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        // Act
        Reservation reservation = jdbcReservationDAO.addReservation(space, 5, LocalDate.now(), LocalDate.now(), "Nick");
        Reservation actualReservation = returnReservationById(reservation.getReservation_id());
        // Assert
        Assert.assertEquals(actualReservation.getReservation_id(), reservation.getReservation_id());
    }

    @Test
    public void list_reservation_with_scheduling_conflict() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        Reservation reservation = insertTestReservation(space, 20, LocalDate.now(), LocalDate.now(), "Jacob");
        // Act
        List<Reservation> reservationList = jdbcReservationDAO.listReservationsBySpaceIdWithScheduleConflict(space, LocalDate.now(), LocalDate.now());
        // Assert
        Assert.assertFalse(reservationList.isEmpty());
    }

    @Test
    public void return_empty_list_reservation_without_scheduling_conflict() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        Reservation reservation = insertTestReservation(space, 20, LocalDate.now(), LocalDate.now(), "Jacob");
        // Act
        List<Reservation> reservationList = jdbcReservationDAO.listReservationsBySpaceIdWithScheduleConflict(space, LocalDate.now().plusDays(5), LocalDate.now().plusDays(5));
        // Assert
        Assert.assertTrue(reservationList.isEmpty());
    }

    private Venue insertTestVenue(String name, long cityId, String description) {
        String sql = "INSERT INTO venue (id, name, city_id, description) VALUES (DEFAULT, ?, ?, ?) RETURNING id";
        Long venueId = jdbcTemplate.queryForObject(sql, Long.class, name, cityId, description);
        Venue venue = new Venue();
        venue.setVenue_id(venueId);
        venue.setName(name);
        venue.setDescription(description);
        return venue;
    }

    private Space insertTestSpace(long venueId, String name, boolean isAccessible, int openFrom, int openTo, BigDecimal dailyRate, int maxOccupancy) {
        String sql = "INSERT INTO space (id, venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        Long spaceId = jdbcTemplate.queryForObject(sql, Long.class, venueId, name, isAccessible, openFrom, openTo, dailyRate, maxOccupancy);
        double dailyRateAsDouble = dailyRate.doubleValue();
        Space space = new Space();
        space.setDailyRate(dailyRateAsDouble);
        space.setAccessible(isAccessible);
        space.setMaxOccupancy(maxOccupancy);
        space.setName(name);
        space.setOpenFrom(openFrom);
        space.setOpenTo(openTo);
        space.setSpaceId(spaceId);
        space.setVenueId(venueId);
        return space;
    }

    private Reservation insertTestReservation(Space space, int numberOfAttendees, LocalDate startDate, LocalDate endDate, String reservedFor) {
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

    private Reservation returnReservationById(long reservationId) {
        String sql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation WHERE reservation_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, reservationId);
        Reservation reservation = new Reservation();
        while(results.next()) {
            reservation.setReservation_id(reservationId);
            reservation.setReservedFor(results.getString("reserved_for"));
            reservation.setEndDate(results.getDate("end_date").toLocalDate());
            reservation.setStartDate(results.getDate("start_date").toLocalDate());
            reservation.setSpace_id(results.getLong("space_id"));
        }
        return reservation;
    }

}
