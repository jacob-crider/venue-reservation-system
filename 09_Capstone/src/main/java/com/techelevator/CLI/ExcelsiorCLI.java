package com.techelevator.CLI;

import javax.sql.DataSource;

import com.techelevator.DAO.VenueDAO;
import com.techelevator.DAO.jdbc.JDBCVenueDAO;
import com.techelevator.view.Menu;
import org.apache.commons.dbcp2.BasicDataSource;

public class ExcelsiorCLI {

	private Menu menu = new Menu();
	private VenueDAO venueDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior_venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		ExcelsiorCLI application = new ExcelsiorCLI(dataSource);
		application.run();
	}

	public ExcelsiorCLI(DataSource datasource) {
		venueDAO = new JDBCVenueDAO(datasource);
	}

	public void run() {

		menu.mainMenu();

		menu.displayVenues(venueDAO.listVenues());
	}
}
