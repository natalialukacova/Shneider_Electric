package gui.controller.employee;

import be.Employees;
import be.Teams;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;

public class UtilizationPController {
    private MainViewController mainController;
    @FXML
    public TextField upTxtField;
    private Employees selectedEmployee;
    private Stage stage;
    private double hourlyRateWithUP;
    private TeamsDAO teamsDAO = new TeamsDAO();


    public void initializeUpTxtField(String initialValue) {
        upTxtField.setText("0");
    }

    public void setHourlyRateWithUP(double hourlyRateWithUP) {
        this.hourlyRateWithUP = hourlyRateWithUP;
    }

    @FXML
    public void confirmAssignEmployee(ActionEvent event) {
        // Employees selectedEmployee = mainController.employeesTableView.getSelectionModel().getSelectedItem();
        Teams selectedTeam = mainController.getSelectedTeam();
        List<Employees> employeesOfTeam = mainController.getEmployeesTeamsDAO().getEmployeesOfTeam(selectedTeam.getId()); // Get the employees of the team


        double utilizationPercentage = Double.parseDouble(upTxtField.getText());
        selectedEmployee.setUtilizationPercentage(utilizationPercentage);

        mainController.getEmployeesTeamsDAO().addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId(), utilizationPercentage);

        // Recalculate and update the team's hourly rate
        double newTeamHourlyRate = mainController.calculateTotalHourlyRateForTeam(employeesOfTeam, Double.parseDouble(upTxtField.getText()));
        selectedTeam.setHourlyRate(newTeamHourlyRate);
        teamsDAO.updateTeam(selectedTeam);

        mainController.loadEmployeesOfTeam(selectedTeam.getId());
        mainController.updateTeamHourlyRateInView(selectedTeam);

        stage.close();
    }

    public String getUpTextFieldValue() {
        return upTxtField.getText();
    }


    public void setSelectedEmployee(Employees selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void cancelBtn(ActionEvent event) {
        closeWindow(event);
    }

    public void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }

    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

}