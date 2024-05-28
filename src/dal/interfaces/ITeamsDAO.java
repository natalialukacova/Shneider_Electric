package dal.interfaces;

import be.Teams;

import java.util.List;

public interface ITeamsDAO {
    void addTeam(Teams teams);
    void updateTeam(Teams teams);

    void addMultipliers(int teamId, double markupMultiplier, double gmMultiplier);

    void deleteTeam(int id);

    List<Teams> getAllTeams();
}
