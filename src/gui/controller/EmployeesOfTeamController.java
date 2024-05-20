package gui.controller;

import dal.EmployeesTeamsDAO;
import gui.utility.ExeptionHandeler;

import javax.swing.*;

public class EmployeesOfTeamController {
    private static EmployeesTeamsDAO employeesTeamsDAO = new EmployeesTeamsDAO();

    public static void assignEmployeeToTeam(int teamId, int employeeId){
        try {
            employeesTeamsDAO.addEmployeeToTeam(teamId, employeeId);
            ExeptionHandeler.showAlert("Employee assigned to the team successfully.");
        } catch (Exception e) {
            ExeptionHandeler.showAlert("Failed to assign employee to the team.");
            e.printStackTrace();
        }
    }


}
