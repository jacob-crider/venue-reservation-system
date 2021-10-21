package com.techelevator.projects.jdbc;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JDBCProjectDAOTest {

    private static SingleConnectionDataSource dataSource;
    private JDBCProjectDAO jdbcProjectDAO;
    private JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        dataSource.setAutoCommit(false);
    }

    @AfterClass
    public static void destroyDataSource() {
        dataSource.destroy();
    }

    @Before
    public void setup() {
        jdbcProjectDAO = new JDBCProjectDAO(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @Test
    public void get_all_active_projects() {
        List<Project> projects = jdbcProjectDAO.getAllActiveProjects();
        int originalCount = projects.size();
        Project project = addTestProject("Project", LocalDate.now(), null);
        List<Project> projectsFromDAO = jdbcProjectDAO.getAllActiveProjects();
        Assert.assertEquals(originalCount +1, projectsFromDAO.size());
    }

    @Test
    public void remove_employee_from_project() {

        Employee employee = insertTestEmployee(2, "Brian", "Lauvray");
        Project project = addTestProject("Test", null, null);
        String sql = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, project.getId(), employee.getId());
        jdbcProjectDAO.removeEmployeeFromProject(project.getId(), employee.getId());
        String sql2 = "SELECT * FROM project_employee WHERE employee_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql2, employee.getId());

        boolean expectedFalse = false;

        if (results.next()) {
            expectedFalse = true;
        }

        Assert.assertFalse(expectedFalse);
    }

    @Test
    public void add_employee_to_project() {

        Employee employee = insertTestEmployee(2, "Brian", "Lauvray");
        Project project = addTestProject("Test", null, null);
        jdbcProjectDAO.addEmployeeToProject(project.getId(), employee.getId());
        String sql2 = "SELECT * FROM project_employee WHERE employee_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql2, employee.getId());

        boolean expectedTrue = false;

        if (results.next()) {
            expectedTrue = true;
        }

        Assert.assertTrue(expectedTrue);

    }

    private Project addTestProject(String name, LocalDate fromDate, LocalDate toDate) {

        Project project = new Project();
        String sql = "INSERT INTO project (project_id, name, from_date, to_date) VALUES (DEFAULT, ?, ?, ?) RETURNING project_id";

        Long projectId = jdbcTemplate.queryForObject(sql, Long.class, name, fromDate, toDate);

        project.setId(projectId);

        return project;
    }

    private Employee insertTestEmployee(long departmentId, String firstName, String lastName) {

        Employee employee = new Employee();
        employee.setDepartmentId(departmentId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setHireDate(LocalDate.now());
        employee.setBirthDay(LocalDate.now());

        String sql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, hire_date) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING employee_id";

        Long employee_id = jdbcTemplate.queryForObject(sql, Long.class, departmentId, firstName, lastName, LocalDate.now(), LocalDate.now());

        employee.setId(employee_id);

        return employee;

    }

}
