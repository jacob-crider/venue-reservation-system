package com.techelevator.DAO;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Space {

    private long spaceId;
    private long venueId;
    private String name;
    private boolean isAccessible;
    private int openFrom;
    private int openTo;
    private double dailyRate;
    private int maxOccupancy;

    public long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(long spaceId) {
        this.spaceId = spaceId;
    }

    public long getVenueId() {
        return venueId;
    }

    public void setVenueId(long venueId) {
        this.venueId = venueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public int getOpenFrom() {
        return openFrom;
    }

    public void setOpenFrom(int openFrom) {
        this.openFrom = openFrom;
    }

    public int getOpenTo() {
        return openTo;
    }

    public void setOpenTo(int openTo) {
        this.openTo = openTo;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    @Override
    public String toString() {
        String nameIndent = "                         ";
        String openIndent = "       ";
        String closeIndent = "        ";
        String dailyRateIndent = "             ";
        return this.getName() + (nameIndent.substring(0, nameIndent.length() - this.getName().length() )) + getMonthAbbrev(this.getOpenFrom()) + (openIndent.substring(0, openIndent.length() - getMonthAbbrev(this.getOpenFrom()).length()) + getMonthAbbrev(this.getOpenTo()) + closeIndent.substring(0, closeIndent.length() - getMonthAbbrev(this.getOpenTo()).length())) + "$" + this.getDailyRate() + dailyRateIndent.substring(0, dailyRateIndent.length() - Double.toString(this.getDailyRate()).length()) + this.getMaxOccupancy();
    }

    private String getMonthAbbrev(int month) {
        List<String> months = new ArrayList<>();
        months.add(" ");
        months.add("Jan.");
        months.add("Feb.");
        months.add("Mar.");
        months.add("Apr.");
        months.add("May");
        months.add("Jun.");
        months.add("Jul.");
        months.add("Aug.");
        months.add("Sep.");
        months.add("Oct.");
        months.add("Nov.");
        months.add("Dec.");

        return months.get(month);
    }
}
