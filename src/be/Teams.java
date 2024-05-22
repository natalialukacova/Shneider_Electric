package be;

import java.util.List;
import java.util.stream.Collectors;

public class Teams {
    private int id;
    private String teamName;
    private Double teamHourlyRate;
    private int countryId;
    private double hourlyRate;
    private double hourlyRateWithMultipliers;

    public double getHourlyRateWithMultipliers() {
        return hourlyRateWithMultipliers;
    }
    public void setHourlyRateWithMultipliers(double hourlyRateWithMultipliers) {
        this.hourlyRateWithMultipliers = hourlyRateWithMultipliers;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTeamHourlyRate() {
        return teamHourlyRate;
    }

    public void setTeamHourlyRate(Double teamHourlyRate) {
        this.teamHourlyRate = teamHourlyRate;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }



    public Teams(int id, String teamName, double teamHourlyRate, int countryId) {
        this.id = id;
        this.teamName = teamName;
        this.teamHourlyRate = teamHourlyRate;
        this.countryId = countryId;
    }

    @Override
    public String toString(){
        return id + "" + teamName;
    }
}
