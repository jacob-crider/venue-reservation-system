package com.techelevator.CLI;

import javax.sql.DataSource;

import com.techelevator.DAO.*;
import com.techelevator.DAO.jdbc.JDBCReservationDAO;
import com.techelevator.DAO.jdbc.JDBCSpaceDAO;
import com.techelevator.DAO.jdbc.JDBCVenueDAO;
import com.techelevator.view.Menu;
import org.apache.commons.dbcp2.BasicDataSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ExcelsiorCLI {

	private Space space = new Space();
	private SpaceDAO spaceDAO;
	private Menu menu = new Menu();
	private VenueDAO venueDAO;
	private boolean menuSwitch = true;
	private ReservationDAO reservationDAO;

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
		reservationDAO = new JDBCReservationDAO(datasource);
	}

	public void run() {

			mainMenuDecision();
	}

	public void mainMenuDecision() {
		loop1: while(true) {
			menuSwitch = true;
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
		loop2: while (menuSwitch) {
			menu.displayVenues(venueDAO.listVenues());
			String userInput = menu.inputFromUser();
			if (userInput.equalsIgnoreCase("R")) {
				break loop2;
			} else if (Integer.parseInt(userInput) - 1 <= venueDAO.listVenues().size()) {
				List<Venue> venues = venueDAO.listVenues();
				Venue venue = venues.get(Integer.parseInt(userInput) - 1);
				menu.displayChosenVenue(venue);
				viewVenueSpaces(venue);
			} else {
				menu.invalidInputMessage();
			}
		}
	}

	public void viewVenueSpaces(Venue venue) {
		loop3: while(menuSwitch) {
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
		loop4: while(menuSwitch) {
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
		loop5: while (menuSwitch) {
			String userInputForDate = menu.askUserForDateNeeded();
			String userInputForDaysNeeded = menu.askUserForDaysNeeded();
			String numberOfAttendees = menu.askUserForAttendees();

			String[] dateArray = userInputForDate.split("/");
			LocalDate startingReservationDate = LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
			LocalDate endingReservationDate = startingReservationDate.plusDays(Integer.parseInt(userInputForDaysNeeded));

			List<Space> spacesAvailable = spaceDAO.listAvailableSpaces(venue, userInputForDate, numberOfAttendees, startingReservationDate, endingReservationDate);
			if (spacesAvailable.isEmpty()) {
					String searchAgain = menu.noSearchResults();
					if (searchAgain.equalsIgnoreCase("Y")) {

					} else {
						menuSwitch = false;
					}
			} else {
				List<Space> spacesWithoutConflictingReservations = new ArrayList<>();
				for (Space space : spacesAvailable) {
					List<Reservation> reservations = reservationDAO.listReservationsBySpaceIdWithScheduleConflict(space, startingReservationDate, endingReservationDate);
					if (reservations.isEmpty()) {
						spacesWithoutConflictingReservations.add(space);
					}
				}
				menu.listSpacesFittingUserNeeds(spacesWithoutConflictingReservations, Integer.parseInt(userInputForDaysNeeded));
				String reserveOrCancelDecision = menu.reserveOrCancel();
				Reservation reservation = new Reservation();
				reservation.setNumberOfAttendees(Integer.parseInt(numberOfAttendees));
				reservation.setEndDate(endingReservationDate);
				reservation.setStartDate(startingReservationDate);
				reservation(reserveOrCancelDecision, spacesWithoutConflictingReservations, reservation, Integer.parseInt(userInputForDaysNeeded));
			}
		}
	}

	public void reservation(String userInput, List<Space> spacesAvailable, Reservation reservation, int numberOfDaysNeeded) {
		loop6: while (menuSwitch) {
			if (userInput.equals("0")) {
					menuSwitch = false;
			} else {
					Space spaceUserSelected = searchListForSpaceId(Long.parseLong(userInput), spacesAvailable);
					if (spaceUserSelected == null) {
						menu.invalidInputMessage();
						break;
					} else {
						String nameOnReservation = menu.reservationName();
						Reservation addedReservation = reservationDAO.addReservation(spaceUserSelected, reservation.getNumberOfAttendees(), reservation.getStartDate(), reservation.getEndDate(), nameOnReservation);
						addedReservation.setTotalCost(spaceUserSelected.getDailyRate() * numberOfDaysNeeded);
						addedReservation.setSpaceName(spaceUserSelected.getName());
						Venue venue = venueDAO.returnVenueBySpaceId(spaceUserSelected.getSpaceId());
						addedReservation.setVenueName(venue.getName());
						menu.reservationDetails(addedReservation);
						menuSwitch = false;
					}
			}
		}
	}

	public Space searchListForSpaceId(long spaceId, List<Space> spacesAvailable) {
		Space spaceInList = null;
		for (Space space : spacesAvailable) {
			if (spaceId == space.getSpaceId()) {
				return space;
			}
		}
		return spaceInList;
	}
}
