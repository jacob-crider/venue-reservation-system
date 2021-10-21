package com.techelevator.view;

import com.techelevator.DAO.Venue;

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

}
