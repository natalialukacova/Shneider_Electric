package gui.controller.team;

import be.Teams;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.awt.*;

public class DeleteTeamController  {
    private MainViewController mainController;
    private Stage stage;
    private Teams selectedTeam;
    private TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    public Label confirmationLabel;


    public void confirmDeleteTeam(){
        teamsDAO.deleteTeam(selectedTeam.getId());

    }

    public void setSelectedTeam(Teams selectedTeam) {
        this.selectedTeam = selectedTeam;
        confirmationLabel.setText("Are you sure you want to delete the team: " + selectedTeam.getTeamName() + "?");
    }


    public void cancelBtn(ActionEvent event) {
    }

    public void closeWindow(ActionEvent event) {
    }
}
