package gui.controller;

import dal.EmployeesTeamsDAO;

import javax.swing.*;

public class EmployeesOfTeamController {
    private static EmployeesTeamsDAO employeesTeamsDAO = new EmployeesTeamsDAO();

    public static void assignEmployeeToTeam(int teamId, int employeeId){
        try {
            employeesTeamsDAO.addEmployeeToTeam(teamId, employeeId);
            JOptionPane.showMessageDialog(null, "Employee assigned to the team successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to assign employee to the team.");
            e.printStackTrace();
        }
    }


}
