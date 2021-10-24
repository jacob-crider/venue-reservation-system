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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCSpaceDAOTest extends DAOIntegrationTest {

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
    public void list_spaces() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        int originalSize = jdbcSpaceDAO.listSpaces(venue).size();
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        Space spaceTwo = insertTestSpace(venue.getVenue_id(), "Jacobs", true, 1, 12, new BigDecimal(500), 500);
        // Act
        int daoSize = jdbcSpaceDAO.listSpaces(venue).size();
        // Assert
        Assert.assertEquals(originalSize + 2, daoSize);
    }

    @Test
    public void over_max_occupancy_space_not_listed() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        // Act
        List<Space> returnedSpaces = jdbcSpaceDAO.listAvailableSpaces(venue, "08/20/2012", "501", LocalDate.now(), LocalDate.now());
        // Assert
        Assert.assertTrue(returnedSpaces.isEmpty());
    }

    @Test
    public void under_max_occupancy_space_listed() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        // Act
        List<Space> returnedSpaces = jdbcSpaceDAO.listAvailableSpaces(venue, "10/23/2021", "40", LocalDate.now(), LocalDate.now());
        // Assert
        Assert.assertFalse(returnedSpaces.isEmpty());
    }

    @Test
    public void does_not_return_space_with_scheduling_conflict() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        Reservation reservation = insertTestReservation(space, 20, LocalDate.now(), LocalDate.now(), "Nick");
        List<Space> returnedSpaces = jdbcSpaceDAO.listAvailableSpaces(venue, "10/24/2021", "40", LocalDate.now(), LocalDate.now());
        List<Space> spacesWithoutConflictingReservations = new ArrayList<>();
        // Act
        for (Space spaceTest : returnedSpaces) {
            List<Reservation> reservations = jdbcReservationDAO.listReservationsBySpaceIdWithScheduleConflict(space, LocalDate.now(), LocalDate.now());
            if (reservations.isEmpty()) {
                spacesWithoutConflictingReservations.add(space);
            }
        }
        // Assert
        Assert.assertTrue(spacesWithoutConflictingReservations.isEmpty());
    }

    @Test
    public void returns_space_without_scheduling_conflict() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        Reservation reservation = insertTestReservation(space, 20, LocalDate.now(), LocalDate.now(), "Nick");
        List<Space> returnedSpaces = jdbcSpaceDAO.listAvailableSpaces(venue, "10/28/2021", "40", LocalDate.now().plusDays(4), LocalDate.now().plusDays(4));
        List<Space> spacesWithoutConflictingReservations = new ArrayList<>();
        // Act
        for (Space spaceTest : returnedSpaces) {
            List<Reservation> reservations = jdbcReservationDAO.listReservationsBySpaceIdWithScheduleConflict(space, LocalDate.now().plusDays(4), LocalDate.now().plusDays(4));
            if (reservations.isEmpty()) {
                spacesWithoutConflictingReservations.add(space);
            }
        }
        // Assert
        Assert.assertFalse(spacesWithoutConflictingReservations.isEmpty());
    }

    @Test
    public void return_space_within_open_months() {
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 1, 12, new BigDecimal(500), 500);
        // Act
        List<Space> returnedSpaces = jdbcSpaceDAO.listAvailableSpaces(venue, "10/23/2021", "40", LocalDate.now(), LocalDate.now());
        // Assert
        Assert.assertFalse(returnedSpaces.isEmpty());
    }

    @Test
    public void does_not_return_space_out_of_open_months() {
        Venue venue = insertTestVenue("Jacobs", 2, "Poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 6, 12, new BigDecimal(500), 500);
        // Act
        List<Space> returnedSpaces = jdbcSpaceDAO.listAvailableSpaces(venue, "5/23/2021", "40", LocalDate.now(), LocalDate.now());
        // Assert
        Assert.assertTrue(returnedSpaces.isEmpty());
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

    private Venue insertTestVenue(String name, long cityId, String description) {
        String sql = "INSERT INTO venue (id, name, city_id, description) VALUES (DEFAULT, ?, ?, ?) RETURNING id";
        Long venueId = jdbcTemplate.queryForObject(sql, Long.class, name, cityId, description);
        Venue venue = new Venue();
        venue.setVenue_id(venueId);
        venue.setName(name);
        venue.setDescription(description);
        return venue;
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
}
