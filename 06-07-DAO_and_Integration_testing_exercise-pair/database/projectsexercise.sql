SELECT department_id, name FROM department;

SELECT department_id, name FROM department WHERE name = ?;

INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id;

UPDATE department SET name = ? WHERE department_id = ?;

SELECT name FROM department WHERE department_id = ?;

SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE first_name = ? AND last_name = ?;

SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE department_id = ?;

SELECT * FROM project_employee;

SELECT DISTINCT employee_id FROM project_employee;

SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE employee_id NOT IN (SELECT DISTINCT employee_id FROM project_employee);

SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE employee_id IN (SELECT employee_id FROM project_employee WHERE project_id = ?);

UPDATE employee SET department_id = ? WHERE employee_id = ?;