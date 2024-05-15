package gui.controller.team;

import be.Countries;
import be.Employees;
import be.Teams;
import dal.CountriesDAO;
import dal.TeamsDAO;
import gui.controller.MainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AddTeamController extends MainViewController {
    @FXML
    private TableView<Teams> teamsTableView;
    public TextField nameTeamTxtField;
    @FXML
    private ComboBox countryBox;
    private CountriesDAO countriesDAO = new CountriesDAO();
    private TeamsDAO teamsDAO = new TeamsDAO();
    private MainViewController mainViewController = new MainViewController();



    public void initialize(){
        loadCountriesBox();
    }

    public void confirmAddTeam(ActionEvent event) {
        String teamName = nameTeamTxtField.getText();
        Countries selectedCountry = (Countries) countryBox.getSelectionModel().getSelectedItem();
        Double teamHourlyRate = null;
        if (selectedCountry == null) {
            System.out.println("No country selected");
            return;
        }
        int countryId = selectedCountry.getCountryId();
        Teams newTeam = new Teams(0, teamName, teamHourlyRate, countryId);
        teamsDAO.addTeam(newTeam);
       // mainViewController.loadTeams();
        closeWindow(event);
    }

    public void loadCountriesBox(){
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

}
