package gui.controller.team;

import be.Countries;
import be.Teams;
import dal.CountriesDAO;
import dal.TeamsDAO;
import gui.utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AddTeamController{

    public TextField nameTeamTxtField;
    @FXML
    private ComboBox countryBox;
    private final CountriesDAO countriesDAO = new CountriesDAO();
    private final TeamsDAO teamsDAO = new TeamsDAO();
    private ObservableList<Teams> teams;


    public void setTeams(ObservableList<Teams> teams){
        this.teams = teams;
    }

    public void initialize(){
        loadCountriesBox();
    }

    public void confirmAddTeam(ActionEvent event) {
        Countries selectedCountry = (Countries) countryBox.getSelectionModel().getSelectedItem();

        if (selectedCountry == null) {
            ExceptionHandler.showAlert("No country selected");
            return;
        }

        String teamName = nameTeamTxtField.getText();
        int countryId = selectedCountry.getCountryId();
        String countryName = selectedCountry.getCountryName();

        double teamHourlyRate = 0;
        double markupMultiplier = 0;
        double gmMultiplier = 0;

        Teams newTeam = new Teams(0, teamName, teamHourlyRate, countryId, markupMultiplier, gmMultiplier, countryName);

        teamsDAO.addTeam(newTeam);
        teams.add(newTeam);
        closeWindow(event);
    }

    private void loadCountriesBox(){
        List<Countries> countries = countriesDAO.getAllCountries();
        ObservableList<Countries> allCountries = FXCollections.observableArrayList(countries);
        countryBox.setItems(allCountries);
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
