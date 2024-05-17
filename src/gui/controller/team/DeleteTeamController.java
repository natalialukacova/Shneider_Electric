package gui.controller.team;

import be.Employees;
import be.Teams;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class DeleteTeamController  {
    private MainViewController mainController;
    private Stage stage;
    private Teams selectedTeam;
    private TeamsDAO teamsDAO = new TeamsDAO();


    public void confirmDeleteTeam(){
       // teamsDAO.deleteTeam();

    }

    public void setSelectedTeam(Teams selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public void cancelBtn(ActionEvent event) {
    }

    public void closeWindow(ActionEvent event) {
    }
}
