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
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import gui.controller.team.addMultiplierController;

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
    private Label hourlyRateWithUpPerTeam;
    @FXML
    private Label hourlyRateWithMultipliersPerTeam;
    @FXML
    private Label sumofHourlyRatePerTeam;
    @FXML
    private Button addMultiplierBtn;
    private EditEmployeeController editEmployeeController;
    private ObservableList<Employees> employeesOfTeamList = FXCollections.observableArrayList();
    private Teams selectedTeam;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");



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

//        //countryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                setTeamsTable(teamsTableView, newSelection.getCountryId());
//                updateTotalHourlyRatesForSelectedCountry();
//            }
//        });


        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                selectedTeam = newSelection;
            loadEmployeesOfTeam(selectedTeam.getId());}
        });

//        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
//        if (selectedCountry != null) {
//            setTeamsTable(teamsTableView, selectedCountry.getCountryId());
//            countryComboBox.setOnAction(event -> updateTotalHourlyRatesForSelectedCountry());
//        }



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
                double hourlyRateWithUP = calculateHourlyRateWithUP(selectedTeam.getId());
                hourlyRateWithUpPerTeam.setText(String.format("%.2f", hourlyRateWithUP)); // Format to two decimal points
            }
        });

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
            double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeam);
            return new SimpleDoubleProperty(totalHourlyRate).asObject();
        });

        teamsTableView.getColumns().addAll(teamNameCol, hourlyRateColumn);

        teamSearch.setTeamsList(teamsOfCountry);
        teamSearch.bindToTeamsTable(teamsTableView);
    }

    // method to calculate total hourly rate per team
    private double calculateTotalHourlyRateForTeam(List<Employees> employeesOfTeam) {
        double totalHourlyRate = 0;
        for (Employees employee : employeesOfTeam) {
            totalHourlyRate += calculateEmployeeHourlyRate(employee);
        }
        return totalHourlyRate;
    }

    // method to calculate employee hourly rate and save it
    public double calculateEmployeeHourlyRate(Employees employee) {
        double annualCost = employee.getSalary() + employee.getConfigurableAmount();
        double totalCost = annualCost + (annualCost * (employee.getMultiplier() / 100)) + employee.getOverheadCost();
        double hourlyRate = totalCost / employee.getWorkingHours();
        return hourlyRate;
    }

    public void updateTotalHourlyRatesForSelectedCountry() {
        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            List<Teams> teamsOfCountry = teamsDAO.getTeamsByCountryId(selectedCountry.getCountryId());
            double totalHourlyRate = 0;
            for (Teams team : teamsOfCountry) {
                List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(team.getId());
                double teamHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeam);
                totalHourlyRate += teamHourlyRate;
            }
            sumofHourlyRatePerTeam.setText(String.format("%.2f", totalHourlyRate)); // Format to two decimal points
        }
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

                // Calculate and display hourly rate with UP% for the team
                double hourlyRateWithUP = calculateHourlyRateWithUP(selectedTeam.getId());
                utilizationPController.setHourlyRateWithUP(hourlyRateWithUP);
                hourlyRateWithUpPerTeam.setText(String.format("%.2f", hourlyRateWithUP)); // Format to two decimal points

                addMultiplierBtn.setDisable(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectedEmployee == null) {
            System.out.println("Error loading FXML file:");
            ExceptionHandler.showAlert("Please select an employee.");
        } else {
            ExceptionHandler.showAlert("Please select a team.");
        }
    }

// Method to calculate hourly rate with UP% for a given team
        private double calculateHourlyRateWithUP(int teamId) {
            double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesTeamsDAO.getEmployeesOfTeam(teamId));
            // Apply UP% (replace 10 with the actual UP% value)
            double hourlyRateWithUP = totalHourlyRate * (1 + (10.0 / 100));
            return hourlyRateWithUP;
        }

    public void updateHourlyRateWithMultipliers(double newHourlyRateWithMultipliers) {
        hourlyRateWithMultipliersPerTeam.setText(String.format("%.2f", newHourlyRateWithMultipliers));
    }

    public void addMultiplierAndRefresh(double hourlyRateMultipliers) {
        if (selectedTeam != null) {
            List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(selectedTeam.getId());
            for (Employees employee : employeesOfTeam) {
                // Assuming there is a method to get and set a multiplier for an employee
                double newMultiplier = employee.getMultiplier() + 10; // Example increment, adjust as needed
                employee.setMultiplier(newMultiplier);
            }
            updateHourlyRateWithMultipliers(selectedTeam.getId()); // Refresh the UI
        }
    }



    private void assignEmployee(){
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null && selectedTeam != null) {
            System.out.println("Assigning Employee ID: " + selectedEmployee.getId() + " to Team ID: " + selectedTeam.getId());
            employeesTeamsDAO.addEmployeeToTeam(selectedTeam.getId(), selectedEmployee.getId());
            loadEmployeesOfTeam(selectedTeam.getId());
        } else if (selectedEmployee == null){
            ExceptionHandler.showAlert("Please select an employee.");
        } else {
            ExceptionHandler.showAlert("Please select a team.");
        }
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
        employeeTeamSearch.setEmployeeTeamList(employeesOfTeam);

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
