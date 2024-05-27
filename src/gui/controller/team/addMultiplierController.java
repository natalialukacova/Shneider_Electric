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

