package gui.controller.team;

import be.Teams;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;

public class DeleteTeamController  {
    private MainViewController mainViewController;
    private Stage stage;
    private Teams selectedTeam;
    private TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    public Label confirmationLabel;


    public void confirmDeleteTeam(){
        teamsDAO.deleteTeam(selectedTeam.getId());
        mainViewController.removeTeamFromTable(selectedTeam);
        closeWindow(new ActionEvent());
    }


    public void setSelectedTeam(Teams selectedTeam) {
        this.selectedTeam = selectedTeam;
        confirmationLabel.setText("Are you sure you want to delete the team: " + selectedTeam.getTeamName() + "?");
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


    public void cancelBtn(ActionEvent event) {
    }

    public void closeWindow(ActionEvent event) {
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
