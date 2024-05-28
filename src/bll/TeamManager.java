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
    public void updateTeam(Teams teams) {
        teamsDAO.updateTeam(teams);
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

    public double calculateTeamHourlyRate(int teamId) {
        List<Employees> employees = employeesTeamsDAO.getEmployeesOfTeam(teamId);
        double totalHourlyRate = 0;
        double totalUtilization = 0;

        for (Employees employee : employees) {
            double hourlyRate = calculateEmployeeHourlyRate(employee);
            double utilization = employee.getUtilizationPercentage() / 100.0;
            totalHourlyRate += hourlyRate * utilization;
            totalUtilization += utilization;
        }

        return totalUtilization > 0 ? totalHourlyRate / totalUtilization : 0;
    }

    public double calculateEmployeeHourlyRate(Employees employee) {
        double annualCost = employee.getSalary() + employee.getConfigurableAmount();
        double totalCost = annualCost + (annualCost * (employee.getMultiplier() / 100)) + employee.getOverheadCost();
        double hourlyRate = totalCost / employee.getWorkingHours();
        return hourlyRate;
    }
}
