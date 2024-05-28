package gui.controller.employee;

import be.Employees;
import be.Teams;
import dal.EmployeesDAO;
import dal.EmployeesTeamsDAO;
import gui.controller.MainViewController;
import gui.utility.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.IOException;

public class UtilizationPController {
    @FXML
    public TextField upTxtField;

    private EmployeesDAO employeesDAO;
    private EmployeesTeamsDAO employeesTeamsDAO;
    private MainViewController mainController;
    private Employees selectedEmployee;
    private Stage stage;
    private double hourlyRateWithUP;


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

    public UtilizationPController(TextField upTxtField) {
        // Add null check for upTxtField
        if (upTxtField != null) {
            this.upTxtField = upTxtField;
        } else {
            // Handle the case when upTxtField is null
            ExceptionHandler.showAlert("Error occurred while initializing UtilizationPController.");
        }
    }
    public double getUpPercentage() {
        // Check if upTxtField is not null before accessing its properties
        if (upTxtField != null) {
            // Now you can safely use upTxtField
            String text = upTxtField.getText();
            // Convert text to double and return
            return Double.parseDouble(text);
        } else {
            // Handle the case when upTxtField is null
            throw new IllegalStateException("upTxtField is null");
        }
    }

    public void setHourlyRateWithUP(double hourlyRateWithUP) {
        this.hourlyRateWithUP = hourlyRateWithUP;
    }


    @FXML
    public void confirmAssignEmployee(ActionEvent event) {
        Teams selectedTeam = mainController.getSelectedTeam();

        double utilizationPercentage = getUpPercentage();
        selectedEmployee.setUtilizationPercentage(utilizationPercentage);

        employeesTeamsDAO.addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId(), utilizationPercentage);
        mainController.loadEmployeesOfTeam(selectedTeam.getId()); // Pass only the team ID

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
