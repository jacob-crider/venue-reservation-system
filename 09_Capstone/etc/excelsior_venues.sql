SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space WHERE venue_id = ?;

SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space WHERE venue_id = ? AND max_occupancy >= ? AND open_from IS NOT NULL AND open_to IS NOT NULL open_from <= ? AND open_to >= ?;

SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal, max_occupancy FROM space WHERE venue_id = ? AND max_occupancy >= ? AND open_from IS NULL AND open_to IS NULL;

