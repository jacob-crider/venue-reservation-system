INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id;

SELECT department_id, name FROM department WHERE department_id = ?;
