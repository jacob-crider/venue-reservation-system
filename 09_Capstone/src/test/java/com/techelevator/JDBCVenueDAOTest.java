package com.techelevator;

import com.techelevator.DAO.Venue;
import com.techelevator.DAO.jdbc.JDBCVenueDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

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


    private Venue insertTestVenue(String name, long cityId, String description) {
        String sql = "INSERT INTO venue (id, name, city_id, description) VALUES (DEFAULT, ?, ?, ?) RETURNING id";
        Long venueId = jdbcTemplate.queryForObject(sql, Long.class, name, cityId, description);
        Venue venue = new Venue();
        venue.setVenue_id(venueId);
        venue.setName(name);
        venue.setDescription(description);
        return venue;
    }

}
