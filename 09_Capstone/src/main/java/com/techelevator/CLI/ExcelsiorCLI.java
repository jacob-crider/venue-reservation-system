package com.techelevator.CLI;

import javax.sql.DataSource;

import com.techelevator.DAO.Space;
import com.techelevator.DAO.SpaceDAO;
import com.techelevator.DAO.Venue;
import com.techelevator.DAO.VenueDAO;
import com.techelevator.DAO.jdbc.JDBCSpaceDAO;
import com.techelevator.DAO.jdbc.JDBCVenueDAO;
import com.techelevator.view.Menu;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;

public class ExcelsiorCLI {

	private Space space = new Space();
	private SpaceDAO spaceDAO;
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
		spaceDAO = new JDBCSpaceDAO(datasource);
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
				viewVenueSpaces(venue);
			} else {
				menu.invalidInputMessage();
			}
		}
	}

	public void viewVenueSpaces(Venue venue) {
		while(true) {
			menu.viewVenueSpacesMenu();
			String userInput = menu.inputFromUser();
			if (userInput.equalsIgnoreCase("R")) {
				break;
			} else if (userInput.equals("1")) {
				reserveSpaceMenu(venue);
			} else {
				menu.invalidInputMessage();
			}
		}
	}

	public void reserveSpaceMenu(Venue venue) {
		while(true) {
			menu.viewSpaces(venue, spaceDAO.listSpaces(venue));
			menu.reserveSpaceMenu();
			String userInput = menu.inputFromUser();
			if (userInput.equalsIgnoreCase("R")) {
				break;
			} else if (userInput.equals("1")) {
				searchForReservationParameters(venue);
			} else {
				menu.invalidInputMessage();
			}
		}
	}

	public void searchForReservationParameters(Venue venue) {
		String userInputForDate = menu.askUserForDateNeeded();
		String userInputForDaysNeeded = menu.askUserForDaysNeeded();
		String numberOfAttendees = menu.askUserForAttendees();

		List<Space> spacesAvailable = spaceDAO.listAvailableSpaces(venue, userInputForDate, numberOfAttendees, userInputForDaysNeeded);
		menu.listSpacesFittingUserNeeds(spacesAvailable, Integer.parseInt(userInputForDaysNeeded));
		menu.inputFromUser();
	}

}
