package be;

public class Teams {
    private int id;
    private String teamName;
    private Double teamHourlyRate;
    private int countryId;
    private String countryName;
    private double markupMultiplier;
    private double gmMultiplier;

    public Teams(double markupMultiplier, double gmMultiplier) {
        this.markupMultiplier = markupMultiplier;
        this.gmMultiplier = gmMultiplier;
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

    public int getCountryId() {
        return countryId;
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
