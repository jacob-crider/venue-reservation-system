SELECT reservation.*, id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id WHERE (? < start_date AND ? < start_date) OR ? > end_date;

SELECT DISTINCT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id WHERE venue_id = ? AND max_occupancy >= ? AND open_from IS NOT NULL AND open_to IS NOT NULL AND open_from <= ? AND open_to >= ? AND ((? < start_date AND ? < start_date) OR ? > end_date);

SELECT DISTINCT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id WHERE venue_id = ? AND max_occupancy >= ? AND open_from IS NULL AND open_to IS NULL AND ((? < start_date AND ? < start_date) OR ? > end_date);


SELECT reservation.*, id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id;

SELECT DISTINCT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space JOIN reservation ON space.id = reservation.space_id;

INSERT INTO reservation (reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING reservation_id;