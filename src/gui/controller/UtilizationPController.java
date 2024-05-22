package gui.controller;

import be.Employees;
import be.Teams;
import dal.EmployeesDAO;
import dal.EmployeesTeamsDAO;
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
    private EmployeesDAO employeesDAO = new EmployeesDAO();
    private EmployeesTeamsDAO employeesTeamsDAO = new EmployeesTeamsDAO();
    private MainViewController mainController = new MainViewController();
    @FXML
    private TextField upTxtField;
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

    public void setHourlyRateWithUP(double hourlyRateWithUP) {
        this.hourlyRateWithUP = hourlyRateWithUP;
    }

    @FXML
    public void confirmAssignEmployee(ActionEvent event) {
       // Employees selectedEmployee = mainController.employeesTableView.getSelectionModel().getSelectedItem();
        Teams selectedTeam = mainController.getSelectedTeam();

        Double utilizationPercentage = Double.parseDouble(upTxtField.getText());
        selectedEmployee.setUtilizationPercentage(Double.parseDouble(upTxtField.getText()));

        employeesTeamsDAO.addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId());
        employeesDAO.updateUtilizationPercentage(selectedEmployee.getId(), utilizationPercentage);
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
