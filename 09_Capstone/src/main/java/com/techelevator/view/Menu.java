package com.techelevator.view;

import com.techelevator.DAO.Reservation;
import com.techelevator.DAO.Space;
import com.techelevator.DAO.Venue;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static Scanner userInput = new Scanner(System.in);

    public String inputFromUser() {
        String userChoice = userInput.nextLine();
        return userChoice;
    }

    public void mainMenu() {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.printf("%20s", "1) List Venues");
        System.out.println();
        System.out.printf("%13s", "Q) Quit");
        System.out.println();
    }

    public void displayVenues(List<Venue> venueList) {
        System.out.println();
        System.out.println("Which venue would you like to view?");
        int counter = 1;
        for (Venue venue : venueList) {
            System.out.printf("%5s %s %s", counter, ") ", venue.getName());
            System.out.println();
            counter++;
        }
        System.out.printf("%20s", "R) Return to Previous Screen");
        System.out.println();
    }

    public void displayChosenVenue(Venue venue) {
        System.out.println(venue);
    }

    public void invalidInputMessage() {
        System.out.println("Please enter a valid input.");
    }

    public void viewVenueSpacesMenu() {
        System.out.println();
        System.out.println("What would you like to do next?");
        System.out.printf("%20s", "1) View Spaces");
        System.out.println();
        System.out.printf("%34s", "R) Return to Previous Screen");
        System.out.println();
    }

    public void viewSpaces(Venue venue, List<Space> spacesList) {
        System.out.println();
        System.out.println(venue.getName());
        System.out.println();
        System.out.println("   Name                     Open   Close   Daily Rate   Max. Occupancy");
        int counter = 1;
        for (Space space : spacesList) {
            String counterFormat = "#" + counter;
            System.out.println(counterFormat + " " + space);
            System.out.println();
            counter++;
        }
    }

    public void reserveSpaceMenu() {
        System.out.println();
        System.out.println("What would you like to do next?");
        System.out.printf("%20s", "1) Reserve a Space");
        System.out.println();
        System.out.printf("%30s", "R) Return to Previous Screen");
        System.out.println();
    }

    public void listSpacesFittingUserNeeds(List<Space> spaceList, int totalDaysNeeded) {
        System.out.println();
        System.out.println("The following spaces are available based on your needs: ");
        System.out.println();
        System.out.println("Space #      Name                        Daily Rate       Max Occup.      Accessible?      Total Cost");
        for(Space space : spaceList) {
            System.out.println(space.availableSpaceFormat(space.getDailyRate(), totalDaysNeeded));
            System.out.println();
        }
    }

    public String askUserForDateNeeded() {
        System.out.println("When do you need the space?");
        return inputFromUser();
    }

    public String askUserForDaysNeeded() {
        System.out.println("How many days will you need the space?");
        return inputFromUser();
    }

    public String askUserForAttendees() {
        System.out.println("How many people will be in attendance?");
        return inputFromUser();
    }

    public String reserveOrCancel() {
        System.out.println();
        System.out.println("Which space would you like to reserve (enter 0 to cancel)?");
        return inputFromUser();
    }

    public String noSearchResults() {
        System.out.println("There are no available spaces given your needs. Search again? (Y) / (N)");
        return inputFromUser();
    }

    public String reservationName() {
        System.out.println("Who is the reservation for?");
        return inputFromUser();
    }

    public void reservationDetails(Reservation reservation) {
        System.out.println();
        System.out.println("Thanks for submitting your reservation! Details for your event are listed below: ");
        System.out.println();
        System.out.println("Confirmation #: " + reservation.getReservation_id());
        System.out.println("Venue: " + reservation.getVenueName());
        System.out.println("Space: " + reservation.getSpaceName());
        System.out.println("Reserved For: " + reservation.getReservedFor());
        System.out.println("Attendees: " + reservation.getNumberOfAttendees());
        System.out.println("Arrival Date: " + reservation.getStartDate());
        System.out.println("Depart Date: " + reservation.getEndDate());
        System.out.println("Total Cost: $" + reservation.getTotalCost());
    }

}
