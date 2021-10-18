package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		String sql = "SELECT project_id, name, from_date, to_date FROM project WHERE from_date IS NOT NULL AND to_date IS NULL";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
		List<Project> projects = new ArrayList<Project>();
		Project project = new Project();

		while(result.next()) {
			project = mapRowToProject(result);
			projects.add(project);
		}

		return projects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sql = "DELETE FROM project_employee WHERE employee_id = ? AND project_id = ?";
		jdbcTemplate.update(sql, employeeId, projectId);
		
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sql = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?)";
		jdbcTemplate.update(sql, projectId, employeeId);
		
	}

	private Project mapRowToProject(SqlRowSet row) {
		Project project = new Project();

		project.setId(row.getLong("project_id"));
		project.setName(row.getString("name"));

		if(row.getDate("from_date") != null) {
			project.setStartDate(row.getDate("from_date").toLocalDate());
		}
		if(row.getDate("to_date") != null) {
			project.setEndDate(row.getDate("to_date").toLocalDate());
		}

		return project;
	}

}
