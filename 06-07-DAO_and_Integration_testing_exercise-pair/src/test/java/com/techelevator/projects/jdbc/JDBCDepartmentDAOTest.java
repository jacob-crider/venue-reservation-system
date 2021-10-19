package com.techelevator.projects.jdbc;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.util.List;

public class JDBCDepartmentDAOTest {

    private static SingleConnectionDataSource dataSource;
    private JDBCDepartmentDAO jdbcDepartmentDAO;
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
        jdbcDepartmentDAO = new JDBCDepartmentDAO(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @Test
    public void get_all_departments() {
        // Arrange
        int originalRowCount = jdbcDepartmentDAO.getAllDepartments().size();
        insertTestDepartment("Nicks");
        insertTestDepartment("Jacobs");

        // Act
        List<Department> retrievedList = jdbcDepartmentDAO.getAllDepartments();

        // Assert
        Assert.assertEquals(originalRowCount + 2, retrievedList.size());

    }

    @Test
    public void search_department_by_name() {
        // Arrange
        Department department = insertTestDepartment("Test");

        // Act
        List<Department> departments = jdbcDepartmentDAO.searchDepartmentsByName("Test");

        // Assert
        Assert.assertEquals(department.getId(), departments.get(0).getId());

    }

    @Test
    public void save_department() {
        // Arrange
        Department department = insertTestDepartment("Test");
        department.setName("Test2");

        // Act
        jdbcDepartmentDAO.saveDepartment(department);

        // Assert
        Assert.assertEquals("Test2", department.getName());

    }

    @Test
    public void create_department() {
        // Arrange
        Department createdDepartment = new Department();
        createdDepartment.setName("Test");

        // Act
        createdDepartment = jdbcDepartmentDAO.createDepartment(createdDepartment);
        Department actualDepartment = selectDepartmentById(createdDepartment.getId());

        // Assert
        Assert.assertEquals(createdDepartment.getId(), actualDepartment.getId());

    }

    @Test
    public void get_department_by_id() {
        // Arrange
        Department department = insertTestDepartment("Test123");

        // Act
        Department departmentFromDAO = jdbcDepartmentDAO.getDepartmentById(department.getId());

        // Assert
        Assert.assertEquals(department.getName(), departmentFromDAO.getName());

    }

    private Department selectDepartmentById(long department_id) {
        Department department = null;
        String sql = "SELECT department_id, name FROM department WHERE department_id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, department_id);

        if (result.next()) {
            department = new Department();
            department.setId(result.getLong("department_id"));
            department.setName(result.getString("name"));
        }

        return department;

    }

    private Department insertTestDepartment(String departmentName) {
        Department department = new Department();
        department.setName(departmentName);

        String sql = "INSERT INTO department (department_id, name) VALUES (DEFAULT, ?) RETURNING department_id";

        Long departmentId = jdbcTemplate.queryForObject(sql, Long.class, departmentName);
        department.setId(departmentId);
        return department;

    }

}
