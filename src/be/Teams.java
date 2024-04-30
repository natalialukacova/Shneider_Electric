package be;

public class Teams {
    private int teamId;
    private String teamName;


    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Teams(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    @Override
    public String toString(){
        return teamId + "" + teamName;
    }
}
