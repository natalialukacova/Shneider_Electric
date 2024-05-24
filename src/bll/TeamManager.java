package bll;

import be.Teams;
import dal.TeamsDAO;
import dal.interfaces.ITeamsDAO;

import java.util.List;

public class TeamManager implements ITeamsDAO {
    ITeamsDAO teamsDAO = new TeamsDAO();

   /* @Override
    public List<Teams> getAllTeams() {
        return teamsDAO.getAllTeams();
    }*/

    @Override
    public List<Teams> getTeamsByCountryId(int countryId) {
        return teamsDAO.getTeamsByCountryId(countryId);
    }

    @Override
    public void addTeam(Teams teams) {
        teamsDAO.addTeam(teams);
    }

    @Override
    public void updateTeam(Teams teams) {
        teamsDAO.updateTeam(teams);
    }

    @Override
    public void addMarkupMultiplier(int teamId, double markupMultiplier) {
        teamsDAO.addMarkupMultiplier(teamId, markupMultiplier);
    }

    @Override
    public void addGmMultiplier(int teamId, double gmMultiplier) {
        teamsDAO.addGmMultiplier(teamId, gmMultiplier);
    }

    @Override
    public void deleteTeam(int id) {
        teamsDAO.deleteTeam(id);
    }

    @Override
    public List<Teams> getAllTeams() {
        return teamsDAO.getAllTeams();
    }

}
