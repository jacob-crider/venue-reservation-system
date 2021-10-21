package com.techelevator.DAO.jdbc;

import com.techelevator.DAO.Category;
import com.techelevator.DAO.Venue;
import com.techelevator.DAO.VenueDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JDBCVenueDAO implements VenueDAO {

    private JdbcTemplate jdbcTemplate;

    public JDBCVenueDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Venue> listVenues() {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT venue.id, venue.name, city.name AS city_name, venue.description, state.abbreviation FROM venue JOIN city ON city.id = venue.city_id JOIN state ON city.state_abbreviation = state.abbreviation";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            venues.add(mapRowToVenue(results));
        }
        return venues;
    }

    private Venue mapRowToVenue(SqlRowSet row) {
        Venue venue = new Venue();
        venue.setVenue_id(row.getLong("id"));
        venue.setName(row.getString("name"));
        venue.setCityName(row.getString("city_name"));
        venue.setDescription(row.getString("description"));
        venue.setStateAbbreviation(row.getString("abbreviation"));

        return venue;
    }

    private List<Category> getVenueCategoryList(long venueId) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category.name, category_id FROM category JOIN category_venue ON category.id = category_venue.category_id JOIN venue ON venue.id = category_venue.venue_id WHERE venue_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, venueId);

        while(results.next()) {
            Category category = new Category();
            category.setCategoryId(results.getLong("id"));
            category.setName(results.getString("name"));
            categories.add(category);
        }

        return categories;
    }
}
