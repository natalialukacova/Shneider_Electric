package gui.controller.employee;

import be.Employees;
import be.Teams;
import dal.EmployeesDAO;
import dal.EmployeesTeamsDAO;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import java.io.IOException;

public class UtilizationPController {
    @FXML
    public TextField upTxtField;

    private EmployeesDAO employeesDAO;
    private EmployeesTeamsDAO employeesTeamsDAO = new EmployeesTeamsDAO();
    private MainViewController mainController;
    private Employees selectedEmployee;
    private Stage stage;
    private double hourlyRateWithUP;
    private TeamsDAO teamsDAO = new TeamsDAO();


    public UtilizationPController() {
        // Ensure MainViewController is properly initialized
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/mainView.fxml"));
            loader.load();
            mainController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  /*  public UtilizationPController(TextField upTxtField) {
            this.upTxtField = upTxtField;
    }*/
    public double getUpPercentage() {
        double up = Double.parseDouble(upTxtField.getText());
        return up;
    }

    public void setHourlyRateWithUP(double hourlyRateWithUP) {
        this.hourlyRateWithUP = hourlyRateWithUP;
    }

    @FXML
    public void confirmAssignEmployee(ActionEvent event) {
        Teams selectedTeam = mainController.getSelectedTeam();

        Double utilizationPercentage = Double.parseDouble(upTxtField.getText());
        selectedEmployee.setUtilizationPercentage(Double.parseDouble(upTxtField.getText()));

        employeesTeamsDAO.addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId(), utilizationPercentage);
        mainController.loadEmployeesOfTeam(selectedTeam.getId());

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
