
SELECT venue.id, venue.name, city.name AS city_name, venue.description, state.abbreviation FROM venue JOIN city ON city.id = venue.city_id JOIN state ON city.state_abbreviation = state.abbreviation;

SELECT category.name, category_id FROM category JOIN category_venue ON category.id = category_venue.category_id JOIN venue ON venue.id = category_venue.venue_id WHERE venue_id = ?;

SELECT venue.id, venue.name, city.name AS city_name, venue.description, state.abbreviation FROM venue JOIN city ON city.id = venue.city_id JOIN state ON city.state_abbreviation = state.abbreviation WHERE venue.id = ?;

SELECT category.name, category_id FROM category JOIN category_venue ON category.id = category_venue.category_id JOIN venue ON venue.id = category_venue.venue_id WHERE venue_id = ?;

INSERT INTO venue (id, name, city_id, description) VALUES (DEFAULT, ?, ?, ?) RETURNING id;

SELECT * FROM venue;

SELECT * FROM venue WHERE id = ?;

SELECT * FROM space;

