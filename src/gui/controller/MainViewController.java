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
import gui.search.EmployeeSearch;
import gui.search.EmployeeTeamSearch;
import gui.search.TeamSearch;
import gui.utility.ExceptionHandler;
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
    public TableView<Employees> employeesTableView;
    @FXML
    public TableColumn<Employees, String> nameColumn;
    @FXML
    public TableColumn<Employees, String> countryColumn;
    @FXML
    private ComboBox<Countries> countryComboBox;
    @FXML
    private TextField employeeSearchField;
    @FXML
    private TextField teamSearchField;
    @FXML
    private TextField employeeTeamSearchField;
    private EmployeeSearch employeeSearch;
    private TeamSearch teamSearch;
    private EmployeeTeamSearch employeeTeamSearch;
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

        employeeSearch = new EmployeeSearch();
        setupEmployeeSearchField();

        teamSearch = new TeamSearch();
        setupTeamSearchField();

        employeeTeamSearch = new EmployeeTeamSearch();
        setupEmployeeTeamSearchField();

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


        updateAverageHourlyRatePerTeam();

    }

    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        List<Employees> employees = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(employees);

        employeesTableView.setItems(observableList);
        employeesTableView.getColumns().clear();

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGeography()));

        employeesTableView.getColumns().addAll(nameColumn, countryColumn);

        employeeSearch.setEmployeesList(employees);
        employeeSearch.bindToEmployeesTable(employeesTableView);
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

        List<Teams> teamsOfCountry = teamsDAO.getTeamsByCountryId(countryId);
        ObservableList<Teams> observableList = FXCollections.observableArrayList(teamsOfCountry);
        teamsTableView.setItems(observableList);

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

        teamSearch.setTeamsList(teamsOfCountry);
        teamSearch.bindToTeamsTable(teamsTableView);
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

    // Method to calculate and display the average hourly rate per team
    private void updateAverageHourlyRatePerTeam() {
        double totalHourlyRate = 0;
        int teamCount = 0;

        // Iterate over all teams in the table view
        for (Teams team : teamsTableView.getItems()) {
            // Get the hourly rate of the current team
            double hourlyRate = team.getTeamHourlyRate();
            // Add to the total hourly rate sum
            totalHourlyRate += hourlyRate;
            // Increment the team count
            teamCount++;
        }

        // Calculate the average hourly rate
        double averageHourlyRate = teamCount > 0 ? totalHourlyRate / teamCount : 0.0;

        // Update the label with the calculated average hourly rate
        averageHourlyRatePerTeam.setText(String.format("%.2f", averageHourlyRate));
    }

    public TableView<Teams> getTeamsTableView() {
        return teamsTableView;
    }

    private void setEmployeesOfTeamTable(TableView<Employees> employeesOfTeamTableView) {
        teamEmployeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        employeesOfTeamTableView.setItems(employeesOfTeamList);

        employeeTeamSearch.bindToEmployeeTeamTable(employeesOfTeamTableView);
    }

    public void assignEmployeeToTeam(ActionEvent event) {
        Employees selectedEmployee = getSelectedEmployee();
        if (selectedEmployee != null && selectedTeam != null) {
            try {
                System.out.println("Loading utilizationPercentage.fxml...");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/utilizationPercentage.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));

                stage.show();

                UtilizationPController utilizationPController = loader.getController();
                utilizationPController.setMainController(this);
                utilizationPController.setStage(stage);
                utilizationPController.setSelectedEmployee(selectedEmployee);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectedEmployee == null){
            System.out.println("Error loading FXML file:");
            ExceptionHandler.showAlert("Please select an employee.");
        } else {
            ExceptionHandler.showAlert("Please select a team.");
        }

    }
    private void assignEmployee(){
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null && selectedTeam != null) {
            System.out.println("Assigning Employee ID: " + selectedEmployee.getId() + " to Team ID: " + selectedTeam.getId());
            employeesTeamsDAO.addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId());
            loadEmployeesOfTeam(selectedTeam.getId());
          //  EmployeesOfTeamController.assignEmployeeToTeam(selectedTeam, selectedEmployee.getId());
        } else if (selectedEmployee == null){
            ExceptionHandler.showAlert("Please select an employee.");
        } else {
            ExceptionHandler.showAlert("Please select a team.");
        }
    }

    public void loadAllEmployees() {
        List<Employees> allEmployees = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(allEmployees);
        employeesTableView.getItems().setAll(observableList);
    }

    public void loadEmployeesOfTeam(int teamId) {
        List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(teamId);
        ObservableList<Employees> observableList = FXCollections.observableArrayList(employeesOfTeam);
        employeesOfTeamList.setAll(observableList);

        employeeTeamSearch.setEmployeeTeamList(employeesOfTeam);

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

    public Employees getSelectedEmployee() {
        return employeesTableView.getSelectionModel().getSelectedItem();
    }

    public Teams getSelectedTeam() {
        return teamsTableView.getSelectionModel().getSelectedItem();
    }

    public void handleCountrySelection(ActionEvent event) {
        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            setTeamsTable(teamsTableView, selectedCountry.getCountryId());
        }
    }

    private void setupEmployeeSearchField() {
        employeeSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            employeeSearch.setSearchCriteria(newValue);
        });
    }

    private void setupTeamSearchField() {
        teamSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            teamSearch.setSearchCriteria(newValue);
        });
    }

    private void setupEmployeeTeamSearchField() {
        employeeTeamSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            employeeTeamSearch.setSearchCriteria(newValue);
        });
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
            ExceptionHandler.showAlert("Please select an employee to edit.");
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
            ExceptionHandler.showAlert("Please select a team to delete.");
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
