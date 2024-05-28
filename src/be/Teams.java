package be;

public class Teams {
    private int id;
    private final String teamName;
    private Double teamHourlyRate;
    private final int countryId;
    private final String countryName;
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

    public String getCountryName() {
        return countryName;
    }

    public double getHourlyRate() { return hourlyRate; }

    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public Teams(int id, String teamName, double teamHourlyRate, int countryId, String countryName) {
        this.id = id;
        this.teamName = teamName;
        this.teamHourlyRate = teamHourlyRate;
        this.countryId = countryId;
        this.countryName = countryName;
    }

    @Override
    public String toString(){
        return id + "" + teamName;
    }
}
