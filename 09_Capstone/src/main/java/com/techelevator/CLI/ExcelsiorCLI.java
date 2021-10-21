package com.techelevator.CLI;

import javax.sql.DataSource;

import com.techelevator.DAO.Venue;
import com.techelevator.DAO.VenueDAO;
import com.techelevator.DAO.jdbc.JDBCVenueDAO;
import com.techelevator.view.Menu;
import org.apache.commons.dbcp2.BasicDataSource;

public class ExcelsiorCLI {

	private Menu menu = new Menu();
	private VenueDAO venueDAO;
	private boolean menuSwitch = true;

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

			mainMenuDecision();

	}

	public void mainMenuDecision() {
		while(true) {
			menu.mainMenu();
			String userInput = menu.inputFromUser();
			if (userInput.equals("1")) {
				venueMenuDecision();
			} else if (userInput.equalsIgnoreCase("Q")) {
					break;
			} else {
				menu.invalidInputMessage();
			}
		}
	}

	public void venueMenuDecision() {
		while (true) {
			menu.displayVenues(venueDAO.listVenues());
			String userInput = menu.inputFromUser();
			if (userInput.equalsIgnoreCase("R")) {
				break;
			} else if (Integer.parseInt(userInput) - 1 <= venueDAO.listVenues().size()) {
				Venue venue = venueDAO.listVenues().get(Integer.parseInt(userInput) - 1);
				menu.displayChosenVenue(venue);
				viewVenueSpaces();
			} else {
				menu.invalidInputMessage();
			}
		}
	}

	public void viewVenueSpaces() {
		while(true) {
			menu.viewVenueSpacesMenu();
			String userInput = menu.inputFromUser();
			if (userInput.equals("R")) {
				break;
			} else if (userInput.equals("1")) {

			} else {
				menu.invalidInputMessage();
			}
		}
	}


}
