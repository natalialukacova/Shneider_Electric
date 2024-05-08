package gui.controller;

import be.Countries;
import be.Teams;
import be.TeamsCountry;
import dal.CountriesDAO;
import dal.TeamsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddTeamController extends MainViewController{
    @FXML
    private TableView<TeamsCountry> teamsTableView;
    public TextField nameTeamTxtField;
    @FXML
    private ComboBox countryComboBox;
    private CountriesDAO countriesDAO = new CountriesDAO();
    private TeamsDAO teamsDAO = new TeamsDAO();
    private MainViewController mainViewController = new MainViewController();



    public void initialize(){
        loadCountries();
    }

    public void confirmAddTeam(ActionEvent event) {
        String teamName = nameTeamTxtField.getText();
        Teams newTeam = new Teams(0, teamName);
        teamsDAO.addTeam(newTeam);
        mainViewController.loadTeams();
        closeWindow(event);
    }

    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void cancelBtn(ActionEvent event) {
        closeWindow(event);
    }

}
