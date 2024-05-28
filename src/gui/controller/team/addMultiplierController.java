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
    private EmployeesDAO employeesDAO;

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

    @FXML
    void addMultipliers(ActionEvent event) {
        try {
            double markupMultiplier = Double.parseDouble(markupMultiplierTxtField.getText());
            double gmMultiplier = Double.parseDouble(gmMultiplierTxtField.getText());

            Teams selectedTeam = mainViewController.getSelectedTeam();

            if (selectedTeam != null) {
                teamsDAO.addMultipliers(selectedTeam.getId(), markupMultiplier, gmMultiplier);
                mainViewController.refresh();
                closeWindow(event);
            } else {
                showError("Please select a team before saving the multipliers.");
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for multipliers.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while saving the multipliers.");
        }
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

