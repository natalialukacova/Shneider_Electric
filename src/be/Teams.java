package be;

public class Teams {
    private int id;
    private String teamName;
    private int countryId;
    private int teamId;


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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public Teams(int id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public Teams(int id, int countryId, int teamId) {
        this.id = id;
        this.countryId = countryId;
        this.teamId = teamId;
    }

    @Override
    public String toString(){
        return id + "" + teamName;
    }
}
