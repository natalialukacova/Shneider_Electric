package gui.controller.employee;

import be.Employees;
import be.Teams;
import dal.EmployeesTeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UtilizationPController {
    @FXML
    private TextField upTxtField;
    private final EmployeesTeamsDAO employeesTeamsDAO = new EmployeesTeamsDAO();
    private MainViewController mainController;
    private Employees selectedEmployee;
    private Stage stage;


    @FXML
    public void confirmAssignEmployee(ActionEvent event) {
        Teams selectedTeam = mainController.getSelectedTeam();

        double utilizationPercentage = Double.parseDouble(upTxtField.getText());
        selectedEmployee.setUtilizationPercentage(Double.parseDouble(upTxtField.getText()));

        employeesTeamsDAO.addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId(), utilizationPercentage);
        mainController.loadEmployeesOfTeam(selectedTeam.getId());

        stage.close();
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
