package dal.interfaces;

import be.Teams;

import java.util.List;

public interface ITeamsDAO {
    List<Teams> getAllTeams();
    void addTeam(Teams teams);
    void updateTeam(Teams teams);
}
