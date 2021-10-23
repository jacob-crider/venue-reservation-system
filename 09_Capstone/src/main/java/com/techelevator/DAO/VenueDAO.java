package com.techelevator.DAO;

import java.util.List;

public interface VenueDAO {

    List<Venue> listVenues();
    Venue returnVenueBySpaceId(long spaceId);
}
