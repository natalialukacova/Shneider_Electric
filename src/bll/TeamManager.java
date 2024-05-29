package bll;

import be.Employees;
import be.Teams;
import dal.TeamsDAO;
import dal.interfaces.ITeamsDAO;
import java.util.List;
import dal.EmployeesTeamsDAO;
import gui.controller.MainViewController;


public class TeamManager implements ITeamsDAO {
    ITeamsDAO teamsDAO = new TeamsDAO();
    private EmployeesTeamsDAO employeesTeamsDAO;
    private MainViewController mainViewController;



    public void setEmployeesTeamsDAO(EmployeesTeamsDAO employeesTeamsDAO) {
        this.employeesTeamsDAO = employeesTeamsDAO;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


    @Override
    public void addTeam(Teams teams) {
        teamsDAO.addTeam(teams);
    }

    @Override
    public void addMultipliers(int teamId, double markupMultiplier, double gmMultiplier) {
        teamsDAO.addMultipliers(teamId, markupMultiplier, gmMultiplier);
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
