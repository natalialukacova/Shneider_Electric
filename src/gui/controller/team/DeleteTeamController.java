package gui.controller.team;

import be.Teams;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class DeleteTeamController  {
    private MainViewController mainViewController;
    private Teams selectedTeam;
    private TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    public Label confirmationLabel;


    public void confirmDeleteTeam(ActionEvent event){
        teamsDAO.deleteTeam(selectedTeam.getId());
        mainViewController.removeTeamFromTable(selectedTeam);
        closeWindow(event);
    }


    public void setSelectedTeam(Teams selectedTeam) {
        this.selectedTeam = selectedTeam;
        confirmationLabel.setText("Are you sure you want to delete the team: " + selectedTeam.getTeamName() + "?");
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }


    public void cancelBtn(ActionEvent event) {
        closeWindow(event);
    }

    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }
}
