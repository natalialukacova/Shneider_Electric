package dal.interfaces;

import be.Teams;

import java.util.List;

public interface ITeamsDAO {
    List<Teams> getTeamsByCountryId(int countryId);
    void addTeam(Teams teams);
    void updateTeam(Teams teams);
    void deleteTeam(int id);
}
