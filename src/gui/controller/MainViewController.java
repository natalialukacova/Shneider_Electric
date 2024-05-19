package gui.controller;

import be.Countries;
import be.Employees;
import be.Teams;
import dal.CountriesDAO;
import dal.EmployeesDAO;
import dal.EmployeesTeamsDAO;
import dal.TeamsDAO;
import gui.controller.employee.AddEmployeeController;
import gui.controller.employee.EditEmployeeController;
import gui.controller.team.DeleteTeamController;
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
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.List;

public class MainViewController {

    private EmployeesDAO employeesDAO;
    private EmployeesTeamsDAO employeesTeamsDAO;
    private final CountriesDAO countriesDAO = new CountriesDAO();
    private TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    public TableView<Teams> teamsTableView;
    @FXML
    private TableColumn<Teams, String> teamNameCol;
    @FXML
    public TableColumn<Teams, Double> hourlyRateColumn;
    @FXML
    public TableView<Employees> employeeOfTeamTableView;
    @FXML
    public TableColumn<Employees, String> teamEmployeeColumn;
    @FXML
    private TableView<Employees> employeesTableView;
    @FXML
    public TableColumn<Employees, String> nameColumn;
    @FXML
    public TableColumn<Employees, String> countryColumn;
    @FXML
    private ComboBox<Countries> countryComboBox;
    private EditEmployeeController editEmployeeController;
    private ObservableList<Employees> employeesOfTeamList = FXCollections.observableArrayList();
    private Teams selectedTeam;


    public void setDependencies(EditEmployeeController editEmployeeController) {
        this.editEmployeeController = editEmployeeController;
    }

    public void initialize() {
        employeesDAO = new EmployeesDAO();
        employeesTeamsDAO = new EmployeesTeamsDAO();

        setEmployeesTable(employeesTableView);
        setEmployeesOfTeamTable(employeeOfTeamTableView);
        loadCountries();

        countryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setTeamsTable(teamsTableView, newSelection.getCountryId());
            }
        });

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                selectedTeam = newSelection;
            loadEmployeesOfTeam(selectedTeam.getId());}
        });

        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            setTeamsTable(teamsTableView, selectedCountry.getCountryId());
        }

    }

    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        List<Employees> employees = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(employees);

        employeesTableView.setItems(observableList);
        employeesTableView.getColumns().clear();

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGeography()));

        employeesTableView.getColumns().addAll(nameColumn, countryColumn);

    }

    public void setTeamsTable(TableView<Teams> teamsTableView, int countryId) {
        if (teamsTableView == null) {
            System.out.println("teamsTableView is null");
            return;
        }

        loadTeams(teamsTableView, countryId);
        teamsTableView.getColumns().clear();

        teamNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamName()));
        hourlyRateColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTeamHourlyRate()).asObject());

        teamsTableView.getColumns().addAll(teamNameCol, hourlyRateColumn);
    }
    public TableView<Teams> getTeamsTableView() {
        return teamsTableView;
    }

    private void setEmployeesOfTeamTable(TableView<Employees> employeesOfTeamTableView) {
        teamEmployeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        employeesOfTeamTableView.setItems(employeesOfTeamList);
    }

    public void assignEmployeeToTeam(ActionEvent event) {
        assignEmployee();
    }
    private void assignEmployee(){
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null && selectedTeam != null) {
            System.out.println("Assigning Employee ID: " + selectedEmployee.getId() + " to Team ID: " + selectedTeam.getId());
            employeesTeamsDAO.addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId());
            loadEmployeesOfTeam(selectedTeam.getId());
          //  EmployeesOfTeamController.assignEmployeeToTeam(selectedTeam, selectedEmployee.getId());
        } else if (selectedEmployee == null){
            showAlert("Please select an employee.");
        } else {
            showAlert("Please select a team.");
        }
    }

    public void loadAllEmployees() {
        List<Employees> allEmployees = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(allEmployees);
        employeesTableView.getItems().setAll(observableList);
    }

    private void loadEmployeesOfTeam(int teamId) {
        List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(teamId);
        ObservableList<Employees> observableList = FXCollections.observableArrayList(employeesOfTeam);
        employeesOfTeamList.setAll(observableList);
    }

    public void loadCountries(){
        ObservableList<Countries> countries = FXCollections.observableArrayList(countriesDAO.getAllCountries());
        countryComboBox.setItems(countries);
    }

    public void loadTeams(TableView<Teams> teamsTableView, int countryId) {
        List<Teams> teamsOfCountry = teamsDAO.getTeamsByCountryId(countryId);
        ObservableList<Teams> observableList = FXCollections.observableArrayList(teamsOfCountry);
        teamsTableView.getItems().setAll(observableList);
    }

    public void handleCountrySelection(ActionEvent event) {
        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            setTeamsTable(teamsTableView, selectedCountry.getCountryId());
        }
    }

    private void setSelectedTeam(){
        selectedTeam = teamsTableView.getSelectionModel().getSelectedItem();
        if (selectedTeam != null) {
            loadEmployeesOfTeam(selectedTeam.getId());
        }
    }

    @FXML
    void addEmplooyeePopUp(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addEmplooyee.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/editEmployee.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));

                EditEmployeeController editEmployeeController = loader.getController();
                editEmployeeController.setMainController(this);
                editEmployeeController.setStage(stage);
                editEmployeeController.setSelectedEmployee(selectedEmployee);

                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select an employee to edit.");
        }
    }

    @FXML
    void addTeamPopUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/addTeam.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteTeamBtn(ActionEvent event) {
        Teams selectedTeam = teamsTableView.getSelectionModel().getSelectedItem();
        if (selectedTeam != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/deleteTeam.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.show();

                DeleteTeamController deleteTeamController = loader.getController();
                deleteTeamController.setSelectedTeam(selectedTeam);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a team to delete.");
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

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

   /** public MainViewController(EditEmployeeController editEmployeeController) {
        this.editEmployeeController = editEmployeeController;
    }**/

}
