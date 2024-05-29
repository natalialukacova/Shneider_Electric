package be;

public class Teams {
    private int id;
    private String teamName;
    private Double teamHourlyRate;
    private int countryId;
    private String countryName;
    private double hourlyRate;
    private double hourlyRateWithMultipliers;
    private double addedUp;
    private double markupMultiplier;
    private double gmMultiplier;

    public Teams(int id, String teamName, double teamHourlyRate, int countryId, String countryName, double markupMultiplier, double gmMultiplier) {
        // Initialize existing fields...
        this.markupMultiplier = markupMultiplier;
        this.gmMultiplier = gmMultiplier;
    }


    public double getHourlyRateWithMultipliers() {
        return hourlyRateWithMultipliers;
    }
    public void setHourlyRateWithMultipliers(double hourlyRateWithMultipliers) {
        this.hourlyRateWithMultipliers = hourlyRateWithMultipliers;
    }

    public double getAddedUp() {
        return addedUp;
    }

    public void setAddedUp(double addedUp) {
        this.addedUp = addedUp;
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

    public String getCountryName() {
        return countryName;
    }

    public double getMarkupMultiplier() {
        return markupMultiplier;
    }

    public void setMarkupMultiplier(double markupMultiplier) {
        this.markupMultiplier = markupMultiplier;
    }

    public double getGmMultiplier() {
        return gmMultiplier;
    }

    public void setGmMultiplier(double gmMultiplier) {
        this.gmMultiplier = gmMultiplier;
    }

    public Teams(int id, String teamName, double teamHourlyRate, int countryId, double markupMultiplier, double gmMultiplier, String countryName) {
        this.id = id;
        this.teamName = teamName;
        this.teamHourlyRate = teamHourlyRate;
        this.countryId = countryId;
        this.markupMultiplier = markupMultiplier;
        this.gmMultiplier = gmMultiplier;
        this.countryName = countryName;
    }

    @Override
    public String toString(){
        return id + "" + teamName;
    }
}
