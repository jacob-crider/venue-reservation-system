package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.techelevator.projects.model.Department;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee";
		List<Employee> employees = new ArrayList<Employee>();

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while(results.next()) {
			employees.add(mapRowToEmployee(results));
		}
		return employees;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE first_name = ? AND last_name = ?";
		List<Employee> employees = new ArrayList<Employee>();

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, firstNameSearch, lastNameSearch);

		Employee employee = new Employee();
		while (results.next()) {
			employee = mapRowToEmployee(results);
			employees.add(employee);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE department_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

		List<Employee> employees = new ArrayList<Employee>();
		Employee employee = new Employee();

		while(result.next()) {
			employee = mapRowToEmployee(result);
			employees.add(employee);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE employee_id NOT IN (SELECT DISTINCT employee_id FROM project_employee)";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql);

		List<Employee> employees = new ArrayList<>();
		Employee employee = new Employee();

		while(result.next()) {
			employee = mapRowToEmployee(result);
			employees.add(employee);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		String sql = "SELECT employee_id, department_id, first_name, last_name, birth_date, hire_date FROM employee WHERE employee_id IN (SELECT employee_id FROM project_employee WHERE project_id = ?)";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, projectId);

		List<Employee> employees = new ArrayList<>();
		Employee employee = new Employee();

		while(result.next()) {
			employee = mapRowToEmployee(result);
			employees.add(employee);
		}
		return employees;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sql = "UPDATE employee SET department_id = ? WHERE employee_id = ?";
		jdbcTemplate.update(sql, departmentId, employeeId);
	}

	private Employee mapRowToEmployee(SqlRowSet row) {
		Employee employee = new Employee();

		employee.setId(row.getLong("employee_id"));
		employee.setFirstName(row.getString("first_name"));
		employee.setLastName(row.getString("last_name"));
		employee.setBirthDay(row.getDate("birth_date").toLocalDate());
		employee.setHireDate(row.getDate("hire_date").toLocalDate());

		if (row.getString("department_id") != null) {
			employee.setDepartmentId(row.getLong("department_id"));
		}
		return employee;
	}

}
