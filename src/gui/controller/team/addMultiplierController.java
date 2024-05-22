package gui.controller.team;

import be.Employees;
import be.Teams;
import dal.EmployeesDAO;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class addMultiplierController {

    private TeamsDAO teamsDAO = new TeamsDAO();
    private EmployeesDAO employeesDAO = new EmployeesDAO();

    @FXML
    private TextField markupMultiplierTxtField;

    @FXML
    private TextField gmMultiplierTxtField;

    @FXML
    private Label errorMessageLabel;

    private MainViewController mainViewController;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void updateHourlyRateWithMultipliers(ActionEvent event) {
        try {
            double markupMultiplier = Double.parseDouble(markupMultiplierTxtField.getText());
            double gmMultiplier = Double.parseDouble(gmMultiplierTxtField.getText());

            Teams selectedTeam = mainViewController.getSelectedTeam();

            if (selectedTeam != null) {
                double newHourlyRateWithUP = calculateTotalHourlyRateWithMultipliers(selectedTeam.getId(), markupMultiplier, gmMultiplier);
                selectedTeam.setHourlyRateWithMultipliers(newHourlyRateWithUP);
                teamsDAO.updateHRwithMultipliers(selectedTeam.getId(),newHourlyRateWithUP);
               // mainViewController.addMultiplierAndRefresh(newHourlyRateWithUP);

                if (mainViewController != null) {
                    mainViewController.updateHourlyRateWithMultipliers(newHourlyRateWithUP);
                }

                closeWindow(event);
            } else {
                showError("Please select a team before calculating the hourly rate.");
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for multipliers.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while calculating the hourly rate.");
        }
    }

    private double calculateTotalHourlyRateWithMultipliers(int teamId, double markupMultiplier, double gmMultiplier) {
        List<Employees> employees = employeesDAO.getEmployeesOfTeam(teamId);

        double totalHourlyRate = 0;
        for (Employees employee : employees) {
            double employeeHourlyRate = calculateEmployeeHourlyRate(employee, markupMultiplier, gmMultiplier);
            totalHourlyRate += employeeHourlyRate;
        }

        return totalHourlyRate;
    }

    private double calculateEmployeeHourlyRate(Employees employee, double markupMultiplier, double gmMultiplier) {
        double hourlyRateWithUP = employee.getHourlyRateWithUP();
        double totalCost = hourlyRateWithUP + (hourlyRateWithUP * (markupMultiplier / 100)) + (hourlyRateWithUP * (gmMultiplier / 100));
        return totalCost;
    }

    private void showError(String message) {
        if (errorMessageLabel != null) {
            errorMessageLabel.setText(message);
        }
    }

    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void cancelBtn(ActionEvent event) {
        closeWindow(event);
    }

    public void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }
}

