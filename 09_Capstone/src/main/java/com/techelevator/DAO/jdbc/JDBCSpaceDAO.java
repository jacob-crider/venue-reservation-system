package com.techelevator.DAO.jdbc;

import com.techelevator.DAO.Space;
import com.techelevator.DAO.SpaceDAO;
import com.techelevator.DAO.Venue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCSpaceDAO implements SpaceDAO {

    private JdbcTemplate jdbcTemplate;

    public JDBCSpaceDAO (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Space> listSpaces(Venue venue) {
        List<Space> spaces = new ArrayList<>();
        String sql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space WHERE venue_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, venue.getVenue_id());
        while(results.next()) {
            spaces.add(mapRowToSpace(results));
        }
        return spaces;
    }

    @Override
    public List<Space> listAvailableSpaces(Venue venue, String dateRequested, String attendees, LocalDate startDate, LocalDate endDate) {
        String[] dateArray = dateRequested.split("/");
        String monthRequested = dateArray[0];

        List<Space> spaces = new ArrayList<>();
        String sql = "SELECT DISTINCT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id WHERE venue_id = ? AND max_occupancy >= ? AND open_from IS NOT NULL AND open_to IS NOT NULL AND open_from <= ? AND open_to >= ? AND ((? < start_date AND ? < start_date) OR ? > end_date) LIMIT 5";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, venue.getVenue_id(), Integer.parseInt(attendees), Integer.parseInt(monthRequested), Integer.parseInt(monthRequested), startDate, endDate, startDate);
        while(results.next()) {
            spaces.add(mapRowToSpace(results));
        }
        String sqlTwo = "SELECT DISTINCT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id WHERE venue_id = ? AND max_occupancy >= ? AND open_from IS NULL AND open_to IS NULL AND ((? < start_date AND ? < start_date) OR ? > end_date) LIMIT 5";
        SqlRowSet resultsTwo = jdbcTemplate.queryForRowSet(sqlTwo, venue.getVenue_id(), Integer.parseInt(attendees), startDate, endDate, startDate);
        while(resultsTwo.next()) {
           Space space = mapRowToSpace(resultsTwo);
           if (!spaces.contains(space)) {
                spaces.add(space);
           }
        }
        return spaces;
    }

    private Space mapRowToSpace(SqlRowSet row) {
        Space space = new Space();
        space.setSpaceId(row.getLong("id"));
        space.setVenueId(row.getLong("venue_id"));
        space.setName(row.getString("name"));
        space.setAccessible(row.getBoolean("is_accessible"));
        space.setDailyRate(row.getDouble("daily_rate"));
        space.setMaxOccupancy(row.getInt("max_occupancy"));
        space.setOpenFrom(row.getInt("open_from"));
        space.setOpenTo(row.getInt("open_to"));

        return space;
    }
}
