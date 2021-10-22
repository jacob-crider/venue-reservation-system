package com.techelevator.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface SpaceDAO {

    List<Space> listSpaces(Venue venue);

    List<Space> listAvailableSpaces(Venue venue, String dateRequested, String attendees, LocalDate startDate, LocalDate endDate);
}
