package gui.controller.employee;

import dal.EmployeesTeamsDAO;
import gui.utility.ExceptionHandler;

public class EmployeesOfTeamController {
    private static EmployeesTeamsDAO employeesTeamsDAO = new EmployeesTeamsDAO();

    public static void assignEmployeeToTeam(int teamId, int employeeId){
        try {
            employeesTeamsDAO.addEmployeeToTeam(teamId, employeeId);
            ExceptionHandler.showAlert("Employee assigned to the team successfully.");
        } catch (Exception e) {
            ExceptionHandler.showAlert("Failed to assign employee to the team.");
            e.printStackTrace();
        }
    }


}
