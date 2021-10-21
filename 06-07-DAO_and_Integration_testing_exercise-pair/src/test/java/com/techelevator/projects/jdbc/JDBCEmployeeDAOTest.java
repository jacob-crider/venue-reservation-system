package com.techelevator.projects.jdbc;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.jdbc.JDBCDepartmentDAO;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JDBCEmployeeDAOTest {

    private static SingleConnectionDataSource dataSource;
    private JDBCEmployeeDAO jdbcEmployeeDAO;
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
        jdbcEmployeeDAO = new JDBCEmployeeDAO(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @Test
    public void get_all_employees() {
        int originalRowCount = jdbcEmployeeDAO.getAllEmployees().size();
        insertTestEmployee(2, "Brian", "Lauvray");
        insertTestEmployee(2, "Rachelle", "Rauh");

        // Act
        List<Employee> retrievedList = jdbcEmployeeDAO.getAllEmployees();

        // Assert
        Assert.assertEquals(originalRowCount + 2, retrievedList.size());
    }

    @Test
    public void get_employees_by_name() {
        // Arrange
        Employee employee = insertTestEmployee(2, "Brian", "Lauvray");

        // Act
        List<Employee> employees = jdbcEmployeeDAO.searchEmployeesByName("Brian", "Lauvray");

        // Assert
        Assert.assertEquals(employee.getLastName(), employees.get(0).getLastName());

    }

    @Test
    public void get_employees_by_department_id() {
        // Arrange
        List<Employee> originalList = jdbcEmployeeDAO.getEmployeesByDepartmentId(2);
        int originalAmount = originalList.size();
        Employee employee = insertTestEmployee(2, "Brian", "Lauvray");

        // Act
        List<Employee> employeesFromDAO = jdbcEmployeeDAO.getEmployeesByDepartmentId(2);

        // Assert
        Assert.assertEquals(originalAmount + 1, employeesFromDAO.size());
    }

    @Test
    public void get_employees_without_projects() {
        // Assert
        List<Employee> originalList = jdbcEmployeeDAO.getEmployeesWithoutProjects();
        int originalCount = originalList.size();
        Employee employee = insertTestEmployee(2, "Brian", "Lauvray");

        // Act
        List<Employee> employeesWithoutProjects = jdbcEmployeeDAO.getEmployeesWithoutProjects();

        // Assert
        Assert.assertEquals(originalCount + 1, employeesWithoutProjects.size());

    }

    @Test
    public void get_employees_by_project_id() {
        // Arrange
        List<Employee> employees = jdbcEmployeeDAO.getEmployeesByProjectId(3L);
        int originalCount = employees.size();
        insertIntoProjectEmployee(3);

        // Act
        List<Employee> employeesFromDAO = jdbcEmployeeDAO.getEmployeesByProjectId(3L);

        // Assert
        Assert.assertEquals(originalCount + 1, employeesFromDAO.size());

    }

    @Test
    public void change_employee_department() {
        // Arrange
        Employee employee = insertTestEmployee(3, "Brian", "Lauvray");
        employee.setDepartmentId(2L);

        // Act
        jdbcEmployeeDAO.changeEmployeeDepartment(employee.getId(), employee.getDepartmentId());
        List<Employee> employees = jdbcEmployeeDAO.searchEmployeesByName("Brian", "Lauvray");
        Employee newEmployee = employees.get(0);

        // Assert
        Assert.assertEquals(2, newEmployee.getDepartmentId());

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

    private Employee insertIntoProjectEmployee(int projectId) {
        Employee employee = insertTestEmployee(2, "Brian", "Lauvray");
        String sql = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, projectId, employee.getId());
        return employee;
    }

}
