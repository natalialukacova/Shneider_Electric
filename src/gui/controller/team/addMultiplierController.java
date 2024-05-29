package gui.controller.team;

import be.Teams;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import gui.utility.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class addMultiplierController {

    private final TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    private TextField markupMultiplierTxtField;
    @FXML
    private TextField gmMultiplierTxtField;
    private MainViewController mainViewController;


    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    void addMultipliers(ActionEvent event) {
        try {
            Teams selectedTeam = mainViewController.getSelectedTeam();

            double markupMultiplier = Double.parseDouble(markupMultiplierTxtField.getText());
            double gmMultiplier = Double.parseDouble(gmMultiplierTxtField.getText());

            selectedTeam.setMarkupMultiplier(Double.parseDouble(markupMultiplierTxtField.getText()));
            selectedTeam.setGmMultiplier(Double.parseDouble(gmMultiplierTxtField.getText()));

            teamsDAO.addMultipliers(selectedTeam.getId(), markupMultiplier, gmMultiplier);
            mainViewController.refreshTeamsTable();
            closeWindow(event);

        } catch (NumberFormatException e) {
            ExceptionHandler.showAlert("Please enter valid numbers for multipliers.");
        } catch (Exception e) {
            ExceptionHandler.showAlert("An error occurred while saving the multipliers.");
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

