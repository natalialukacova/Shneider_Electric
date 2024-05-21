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
import gui.utility.ExeptionHandeler;
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
    @FXML
    private Label hourlyRateNoMultipliers;
    @FXML
    private Label hourlyRateWithMultipliers;
    @FXML
    private Label averageHourlyRatePerTeam;
    @FXML
    private Button addMultiplierBtn;
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

    private double calculateAverageHourlyRate(List<Employees> employees) {
        if (employees.isEmpty()) {
            return 0;
        }
        double totalHourlyRate = 0;
        for (Employees employee : employees) {
            totalHourlyRate += employee.getHourlyRate();
        }
        return totalHourlyRate / employees.size();
    }

    public void setTeamsTable(TableView<Teams> teamsTableView, int countryId) {
        if (teamsTableView == null) {
            System.out.println("teamsTableView is null");
            return;
        }

        loadTeams(teamsTableView, countryId);
        teamsTableView.getColumns().clear();

        teamNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamName()));
        hourlyRateColumn.setCellValueFactory(cellData -> {
            // Get employees of the team
            List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(cellData.getValue().getId());
            // Calculate average hourly rate per team
            double averageHourlyRate = calculateAverageHourlyRate(employeesOfTeam);
            return new SimpleDoubleProperty(averageHourlyRate).asObject();
        });

        teamsTableView.getColumns().addAll(teamNameCol, hourlyRateColumn);
    }

    // New method to calculate employee hourly rate and save it
    private double calculateEmployeeHourlyRate(Employees employee) {
        double annualCost = employee.getSalary() + employee.getConfigurableAmount();
        double totalCost = annualCost + (annualCost * (employee.getMultiplier() / 100)) + employee.getOverheadCost();
        double hourlyRate = totalCost / employee.getWorkingHours();
        employeesDAO.updateEmployeeHourlyRate(employee.getId(), hourlyRate); // Save hourly rate to DB
        return hourlyRate;
    }

    // New method to calculate and save team hourly rate
    private double calculateAndSaveTeamHourlyRate(int teamId, List<Employees> employeesOfTeam) {
        double totalHourlyRate = 0;
        for (Employees employee : employeesOfTeam) {
            double hourlyRate = calculateEmployeeHourlyRate(employee);
            totalHourlyRate += hourlyRate;
        }
        double averageHourlyRate = employeesOfTeam.size() > 0 ? totalHourlyRate / employeesOfTeam.size() : 0;
        teamsDAO.updateTeamHourlyRate(teamId, averageHourlyRate); // Ensure team hourly rate is saved to DB
        return averageHourlyRate;
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
            ExeptionHandeler.showAlert("Please select an employee.");
        } else {
            ExeptionHandeler.showAlert("Please select a team.");
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

        // Set the calculated hourly rate for the selected team
        double averageHourlyRate = calculateAverageHourlyRate(employeesOfTeam);
        selectedTeam.setTeamHourlyRate(averageHourlyRate);
    }

    public void loadCountries(){
        ObservableList<Countries> countries = FXCollections.observableArrayList(countriesDAO.getAllCountries());
        countryComboBox.setItems(countries);
    }

    public void removeTeamFromTable(Teams team) {
        teamsTableView.getItems().remove(team);
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
    void addMultiplierPopUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/addMultiplier.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
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
            ExeptionHandeler.showAlert("Please select an employee to edit.");
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
                deleteTeamController.setMainViewController(this);
                deleteTeamController.setSelectedTeam(selectedTeam);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ExeptionHandeler.showAlert("Please select a team to delete.");
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    @FXML
    public void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }



}
