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
import gui.controller.employee.UtilizationPController;
import gui.controller.team.AddTeamController;
import gui.controller.team.DeleteTeamController;
import gui.search.EmployeeSearch;
import gui.search.EmployeeTeamSearch;
import gui.search.TeamSearch;
import gui.utility.ExceptionHandler;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.controller.team.addMultiplierController;

public class MainViewController {
    private EmployeesDAO employeesDAO;
    private EmployeesTeamsDAO employeesTeamsDAO;
    private CountriesDAO countriesDAO = new CountriesDAO();
    private TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    public TableView<Teams> teamsTableView;
    @FXML
    private TableColumn<Teams, String> teamNameCol;
    @FXML
    private TableColumn<Teams, String> teamCountryCol;
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
    private TableColumn<Teams, Double> addedMultipliersColumn;

    @FXML
    private TableColumn<Teams, Double> addedUPColumn;
    @FXML
    private ComboBox<Countries> countryComboBox;
    @FXML
    private TextField employeeSearchField;
    @FXML
    private TextField teamSearchField;
    @FXML
    private TextField employeeTeamSearchField;
    @FXML
    private TextField upTxtField;
    @FXML
    private TextField markupMultiplierTxtField;
    @FXML
    private TextField gmMultiplierTxtField;

    private EmployeeSearch employeeSearch;
    private TeamSearch teamSearch;
    private EmployeeTeamSearch employeeTeamSearch;
    @FXML
    private Button addMultiplierBtn;
    private EditEmployeeController editEmployeeController;
    private ObservableList<Employees> employeesOfTeamList = FXCollections.observableArrayList();
    private ObservableList<Employees> allEmployees;
    private ObservableList<Employees> allEmployeesOfTeam;
    private ObservableList<Teams> allTeams;
    private Teams selectedTeam;
    private UtilizationPController utilizationPController;

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    // Field to store UP percentage for each team

    public void refresh() {
        //loadTeams();
    }

    public void setDependencies(EditEmployeeController editEmployeeController) {
        this.editEmployeeController = editEmployeeController;
    }

    public void initialize() {

        // Pass the TextField to UtilizationPController constructor
       // utilizationPController = new UtilizationPController(upTxtField);

        employeesDAO = new EmployeesDAO();
        employeesTeamsDAO = new EmployeesTeamsDAO();

        employeeSearch = new EmployeeSearch();

        teamSearch = new TeamSearch();

        employeeTeamSearch = new EmployeeTeamSearch();


        setEmployeesTable(employeesTableView);
        setEmployeesOfTeamTable(employeeOfTeamTableView);
        setTeamsTable(teamsTableView);
        loadCountries();
       // loadTeams();

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                selectedTeam = newSelection;
                loadEmployeesOfTeam(selectedTeam.getId());}
        });

        addedUPColumn.setCellValueFactory(new PropertyValueFactory<>("addedUp"));

    }


    // Method to load teams and initialize upPercentageMap
    /*private void loadTeams() {
        allTeams = FXCollections.observableArrayList(teamsDAO.getAllTeams());
        teamsTableView.setItems(allTeams);

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTeam = newSelection;
                //updateAddedUPColumn(selectedTeam.getId());
            }
        });


        // Setup DecimalFormatter for hourlyRateColumn
        hourlyRateColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(decimalFormat.format(item));
                }
            }
        });

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTeam = newSelection;
                // Calculate and display hourly rate with UP% for the selected team
                updateAddedUPColumn(selectedTeam.getId());

            }
        });

    }*/

    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        allEmployees = FXCollections.observableArrayList(employeesDAO.getAllEmployees());
        employeesTableView.setItems(allEmployees);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGeography()));

        employeeSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            EmployeeSearch.search(employeesTableView, allEmployees, newValue);
        });
    }


    public void setTeamsTable(TableView<Teams> teamsTableView) {
        allTeams = FXCollections.observableArrayList(teamsDAO.getAllTeams());
        teamsTableView.setItems(allTeams);

        teamNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamName()));
        teamCountryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountryName()));
        hourlyRateColumn.setCellValueFactory(cellData -> {
            List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(cellData.getValue().getId());
            double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeam);
            return new SimpleDoubleProperty(totalHourlyRate).asObject();
        });

        addedUPColumn.setCellValueFactory(cellData -> {
            List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(cellData.getValue().getId());
            double upPercentage = utilizationPController.getUpPercentage();
            double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeamList);
            double hourlyRateWithUP = totalHourlyRate * (1 + (upPercentage / 100));
            for (Teams team : teamsTableView.getItems()) {
                if (team.getId() == selectedTeam.getId()) {
                team.setAddedUp(hourlyRateWithUP);
                teamsTableView.refresh();
                break;
                }
            }
            return new SimpleDoubleProperty(hourlyRateWithUP).asObject();
        } ); // Updated column reference


        teamSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    TeamSearch.search(teamsTableView, allTeams, newValue);
        });
    }

    private void setEmployeesOfTeamTable(TableView<Employees> employeesOfTeamTableView) {
        teamEmployeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        employeesOfTeamTableView.setItems(employeesOfTeamList);

        employeeTeamSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            EmployeeSearch.search(employeesOfTeamTableView, employeesOfTeamList, newValue);
        });
    }

    // method to calculate total hourly rate per team
    public double calculateTotalHourlyRateForTeam(List<Employees> employeesOfTeam) {
        //double upPercentage = employeesTeamsDAO.getUPForEmployeeeOnTeam(team, employee);
        double totalHourlyRate = 0;
        for (Employees employee : employeesOfTeam) {
            totalHourlyRate += calculateEmployeeHourlyRate(employee);
        }
        return totalHourlyRate * upPercentage/100;
    }

    // method to calculate employee hourly rate and save it
    public double calculateEmployeeHourlyRate(Employees employee) {
        double annualCost = employee.getSalary() + employee.getConfigurableAmount();
        double totalCost = annualCost + (annualCost * (employee.getMultiplier() / 100)) + employee.getOverheadCost();
        double hourlyRate = totalCost / employee.getWorkingHours();


        return hourlyRate;
    }

    // New method to update added UP column for a given team
    private void updateAddedUPColumn(int teamId) {
        if (utilizationPController == null) {
            // Initialize the utilizationPController variable
            utilizationPController = new UtilizationPController();
        }
        // Now it's safe to use utilizationPController
        double upPercentage = utilizationPController.getUpPercentage();
        double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeamList);
        double hourlyRateWithUP = totalHourlyRate * (1 + (upPercentage / 100));
        for (Teams team : teamsTableView.getItems()) {
            if (team.getId() == teamId) {
                team.setAddedUp(hourlyRateWithUP);
                teamsTableView.refresh();
                break;
            }
        }
    }


    public void assignEmployeeToTeam(ActionEvent event) {
        Employees selectedEmployee = getSelectedEmployee();
        if (selectedEmployee != null && selectedTeam != null) {
            try {
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

                /*stage.setOnHiding(event1 -> {
                    double upPercentage = utilizationPController.getUpPercentage();

                    // Refresh the team table to reflect the new UP percentage
                    updateAddedUPColumn(selectedTeam.getId());

                    addMultiplierBtn.setDisable(false);
                });*/


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectedEmployee == null) {
            ExceptionHandler.showAlert("Please select an employee.");
        } else {
            ExceptionHandler.showAlert("Please select a team.");
        }
    }

    private double calculateHourlyRateWithUP(int teamId, double upPercentage) {
        // Fetch the employees of the team using the teamId
        List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(teamId);

        // Calculate the total hourly rate for the team
        double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeam);

        // Calculate the hourly rate with UP percentage
        double hourlyRateWithUP = totalHourlyRate * (1 + (upPercentage / 100));

        return hourlyRateWithUP;
    }


    private double calculateHourlyRateWithMultipliers(int teamId, double markupMultiplier, double gmMultiplier) {
        // Fetch the employees of the team using the teamId
        List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(teamId);

        // Calculate the total hourly rate for the team
        double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeam);

        // Calculate the hourly rate with markup and GM multipliers
        double hourlyRateWithMultipliers = totalHourlyRate * (1 + (markupMultiplier / 100)) * (1 + (gmMultiplier / 100));

        return hourlyRateWithMultipliers;
    }

    private void addMultipliers(Teams selectedTeam) {
        // Retrieve markup and GM multipliers from text fields
        double markupMultiplier = Double.parseDouble(markupMultiplierTxtField.getText());
        double gmMultiplier = Double.parseDouble(gmMultiplierTxtField.getText());

        if (selectedTeam != null) {
            // Recalculate hourly rate with UP percentage
            double upPercentage = utilizationPController.getUpPercentage();
            double hourlyRateWithUP = calculateHourlyRateWithUP(selectedTeam.getId(), upPercentage);

            // Apply multipliers to the hourly rate with UP
            double hourlyRateWithMultipliers = hourlyRateWithUP * (1 + (markupMultiplier / 100)) * (1 + (gmMultiplier / 100));

            // Update the hourly rate with multipliers in the selected team
            updateHourlyRateWithMultipliers(selectedTeam, hourlyRateWithMultipliers);

            // Refresh the teams table
            //loadTeams();
        }

}
    private void updateHourlyRateWithMultipliers(Teams selectedTeam, double hourlyRateWithMultipliers) {
        for (Teams team : teamsTableView.getItems()) {
            if (team.getId() == selectedTeam.getId()) {
                team.setHourlyRateWithMultipliers(hourlyRateWithMultipliers);
                break;
            }
        }
        teamsTableView.refresh();
    }



    public void loadAllEmployees() {
        List<Employees> allEmployees = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(allEmployees);
        employeesTableView.setItems(observableList);
    }

    public void loadEmployeesOfTeam(int teamId) {
        List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(teamId);
        ObservableList<Employees> observableList = FXCollections.observableArrayList(employeesOfTeam);
        employeesOfTeamList.setAll(observableList);

    }

    public void loadCountries(){
        ObservableList<Countries> countries = FXCollections.observableArrayList(countriesDAO.getAllCountries());
       // countryComboBox.setItems(countries);
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


    @FXML
    void addEmplooyeePopUp(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addEmplooyee.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));

            AddEmployeeController addEmployeeController = loader.getController();
            addEmployeeController.setEmployees(employeesTableView.getItems());
            addEmployeeController.setMainController(this);
            addEmployeeController.setStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void addMultiplierPopUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/view/addMultiplier.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();

            addMultiplierController controller = loader.getController();
            controller.setMainViewController(this);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addTeam.fxml"));
            Parent root = loader.load();
            AddTeamController tc = loader.getController();
            tc.setTeams(teamsTableView.getItems());
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
