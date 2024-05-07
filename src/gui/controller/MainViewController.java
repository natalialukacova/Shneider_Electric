package gui.controller;

import be.Countries;
import be.Employees;
import be.Teams;
import be.TeamsCountry;
import dal.CountriesDAO;
import dal.EmployeesDAO;
import dal.TeamsCountriesDAO;
import dal.TeamsDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainViewController {

    private EmployeesDAO employeesDAO;
    @FXML
    private TableView<Teams> teamsTableView;
    @FXML
    private TableColumn<Teams, String> teamNameCol;
    @FXML
    private TableView<Employees> employeesTableView;
    @FXML
    public TableColumn<Employees, String> nameColumn;
    @FXML
    public TableColumn<Employees, Double> hourlyRateColumn;
    @FXML
    private ComboBox<Countries> countryComboBox;
    private final CountriesDAO countriesDAO = new CountriesDAO();
    private TeamsDAO teamsDAO = new TeamsDAO();
    private TeamsCountriesDAO teamsCountriesDAO = new TeamsCountriesDAO();



    public void initialize() {
        employeesDAO = new EmployeesDAO();
        setEmployeesTable(employeesTableView);
        loadCountries();
        setTeamsTable(teamsTableView);
        loadTeams();

    }

    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        List<Employees> employees = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(employees);
        employeesTableView.setItems(observableList);

        // Clear existing columns
        employeesTableView.getColumns().clear();

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));

        employeesTableView.getColumns().addAll(nameColumn, hourlyRateColumn);
    }

    public void setTeamsTable(TableView<Teams> teamsTableView) {
        List<Teams> teamsOfCountry = teamsCountriesDAO.getAllTeamsOfCountry();
        ObservableList<Teams> observableList = FXCollections.observableArrayList(teamsOfCountry);
        teamsTableView.setItems(observableList);
        teamsTableView.getColumns().clear();
        teamNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamName()));
        teamsTableView.getColumns().addAll(teamNameCol);
    }

    @FXML
    void addEmplooyeePopUp(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addEmplooyee.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            AddEmployeeController addEmployeeController = loader.getController();
            addEmployeeController.setMainController(this);
            addEmployeeController.setStage(stage);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void editEmployeeBtn(ActionEvent event) {
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addEmplooyee.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                AddEmployeeController addEmployeeController = loader.getController();
                addEmployeeController.setMainController(this);
                addEmployeeController.setStage(stage);
                addEmployeeController.setSelectedEmployee(selectedEmployee);

                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select an employee to edit.");
        }
    }

    public void loadAllEmployees() {
        EmployeesDAO employeeDAO = new EmployeesDAO();
        List<Employees> allEmployees = employeeDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(allEmployees);
        employeesTableView.getItems().setAll(allEmployees);
    }

    public void loadCountries(){
        ObservableList<Countries> countries = FXCollections.observableArrayList(countriesDAO.getAllCountries());
        countryComboBox.setItems(countries);
    }

    public void loadTeams(){
        System.out.println("Attempting to load teams...");
        if (teamsTableView == null) {
            System.err.println("TableView not initialized");
            return;
        }

        try {
            List<Teams> teamsOfCountry = teamsCountriesDAO.getAllTeamsOfCountry();
            ObservableList<Teams> observableList = FXCollections.observableArrayList(teamsOfCountry);

            // Update the TableView on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                teamsTableView.setItems(observableList);
            });
        } catch (Exception e) {
            System.err.println("Error loading teams: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void addTeamPopUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/addTeam.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
