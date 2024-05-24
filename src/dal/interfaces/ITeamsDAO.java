package dal.interfaces;

import be.Teams;

import java.util.List;

public interface ITeamsDAO {
    List<Teams> getTeamsByCountryId(int countryId);
    void addTeam(Teams teams);
    void updateTeam(Teams teams);

    void addMarkupMultiplier(int teamId, double markupMultiplier);

    void addGmMultiplier(int teamId, double gmMultiplier);

    void deleteTeam(int id);

    List<Teams> getAllTeams();
}
