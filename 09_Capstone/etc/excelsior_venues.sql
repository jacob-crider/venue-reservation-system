SELECT DISTINCT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id WHERE venue_id = ? AND max_occupancy >= ? AND open_from IS NOT NULL AND open_to IS NOT NULL AND open_from <= ? AND open_to >= ?;

SELECT DISTINCT venue.id AS venue_id, space.id, reservation.*, venue_id, space.name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id JOIN venue ON venue.id = space.venue_id;
