package com.techelevator;

import com.techelevator.DAO.Space;
import com.techelevator.DAO.Venue;
import com.techelevator.DAO.jdbc.JDBCVenueDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JDBCVenueDAOTest extends DAOIntegrationTest {

    private JDBCVenueDAO jdbcVenueDAO;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        jdbcVenueDAO = new JDBCVenueDAO(getDataSource());
        jdbcTemplate = new JdbcTemplate(getDataSource());
    }

    @Test
    public void list_venues() {
        // Arrange
        int originalSize = jdbcVenueDAO.listVenues().size();
        insertTestVenue("Jacobs", 3, "Poppin");
        insertTestVenue("Nicks", 3, "Crackin");
        // Act
        int daoSize = jdbcVenueDAO.listVenues().size();
        // Assert
        Assert.assertEquals(originalSize + 2, daoSize);
    }

    @Test
    public void return_venue_by_space_id() {
        // Arrange
        Venue venue = insertTestVenue("Jacobs", 2, "It's poppin");
        Space space = insertTestSpace(venue.getVenue_id(), "Nicks", true, 0, 0, new BigDecimal(500), 500);
        // Act
        Venue returnedVenue = jdbcVenueDAO.returnVenueBySpaceId(space.getSpaceId());
        // Assert
        Assert.assertEquals(venue.getVenue_id(), returnedVenue.getVenue_id());
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


}
