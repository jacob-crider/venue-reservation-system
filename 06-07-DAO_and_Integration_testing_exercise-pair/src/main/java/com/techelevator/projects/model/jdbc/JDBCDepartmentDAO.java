package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		String sql = "SELECT department_id, name FROM department";
		List<Department> departments = new ArrayList<Department>();

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while(results.next()) {
			departments.add(mapRowToDepartment(results));
		}
		return departments;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		String sql = "SELECT department_id, name FROM department WHERE name = ?";
		List<Department> departments = new ArrayList<Department>();

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, nameSearch);

		while(results.next()) {
			departments.add(mapRowToDepartment(results));
		}
		return departments;
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String sql = "UPDATE department SET name = ? WHERE department_id = ?";
		jdbcTemplate.update(sql, updatedDepartment.getName(), updatedDepartment.getId());
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String sql = "INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id";
		Long departmentId = jdbcTemplate.queryForObject(sql, Long.class, newDepartment.getName());

		newDepartment.setId(departmentId);

		return newDepartment;
	}

	@Override
	public Department getDepartmentById(Long id) {
		String sql = "SELECT name FROM department WHERE department_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

		Department department = new Department();

		while(result.next()) {
			department = mapRowToDepartment(result);

		}
		return department;
	}

	private Department mapRowToDepartment(SqlRowSet row) {
		Department department = new Department();

		department.setId(row.getLong("department_id"));
		department.setName(row.getString("name"));

		return department;
	}


}
