package gui.controller;

import be.Employees;
import be.Teams;
import bll.TeamManager;
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
import gui.controller.team.addMultiplierController;

public class MainViewController {
    private EmployeesDAO employeesDAO;
    private EmployeesTeamsDAO employeesTeamsDAO;
    private final TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    private TableView<Teams> teamsTableView;
    @FXML
    private TableColumn<Teams, String> teamNameCol;
    @FXML
    private TableColumn<Teams, String> teamCountryCol;
    @FXML
    private TableColumn<Teams, Double> hourlyRateColumn;
    @FXML
    private TableView<Employees> employeeOfTeamTableView;
    @FXML
    private TableColumn<Employees, String> teamEmployeeColumn;
    @FXML
    private TableView<Employees> employeesTableView;
    @FXML
    private TableColumn<Employees, String> nameColumn;
    @FXML
    private TableColumn<Employees, String> countryColumn;
    @FXML
    private TableColumn<Teams, Double> markupColumn;
    @FXML
    private TableColumn<Teams, Double> gmColumn;
    @FXML
    private TextField employeeSearchField;
    @FXML
    private TextField teamSearchField;
    @FXML
    private TextField employeeTeamSearchField;
    private final ObservableList<Employees> employeesOfTeamList = FXCollections.observableArrayList();
    private ObservableList<Employees> allEmployees;
    private ObservableList<Teams> allTeams;
    private Teams selectedTeam;
    private TeamManager teamManager;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private UtilizationPController utilizationPController;

    public void refreshTeamsTable() {
        allTeams.setAll(teamsDAO.getAllTeams());
    }

    public void setEmployeesTeamsDAO(EmployeesTeamsDAO employeesTeamsDAO) {
        this.employeesTeamsDAO = employeesTeamsDAO;
    }

    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }


    public void initialize() {
        employeesDAO = new EmployeesDAO();
        employeesTeamsDAO = new EmployeesTeamsDAO();

        setEmployeesTable(employeesTableView);
        setEmployeesOfTeamTable(employeeOfTeamTableView);
        setTeamsTable(teamsTableView);

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTeam = newSelection;
                loadEmployeesOfTeam(selectedTeam.getId());
            }
        });

        setupHourlyRateColumnFormatter();
    }


    // Setup DecimalFormatter for hourlyRateColumn
    private void setupHourlyRateColumnFormatter() {
        hourlyRateColumn.setCellFactory(tc -> new TableCell<>() {
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(decimalFormat.format(item));
                }
            }
        });
    }



    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        allEmployees = FXCollections.observableArrayList(employeesDAO.getAllEmployees());
        employeesTableView.setItems(allEmployees);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGeography()));

        employeeSearchField.textProperty().addListener((observable, oldValue, newValue) ->
                EmployeeSearch.search(employeesTableView, allEmployees, newValue));
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

        gmColumn.setCellValueFactory(cellData ->
             new SimpleDoubleProperty(cellData.getValue().getGmMultiplier()).asObject()
        );
        markupColumn.setCellValueFactory(cellData ->
             new SimpleDoubleProperty(cellData.getValue().getMarkupMultiplier()).asObject()
        );


        teamSearchField.textProperty().addListener((observable, oldValue, newValue) ->
                    TeamSearch.search(teamsTableView, allTeams, newValue)
        );
    }

    private void setEmployeesOfTeamTable(TableView<Employees> employeesOfTeamTableView) {
        teamEmployeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        employeesOfTeamTableView.setItems(employeesOfTeamList);

        employeeTeamSearchField.textProperty().addListener((observable, oldValue, newValue) ->
            EmployeeTeamSearch.search(employeesOfTeamTableView, employeesOfTeamList, newValue));
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

                this.utilizationPController = loader.getController();
                utilizationPController.setMainController(this);
                utilizationPController.setStage(stage);
                utilizationPController.setSelectedEmployee(selectedEmployee);


            } catch (IOException e) {
                ExceptionHandler.showAlert("Operation Failed");
            }
        } else if (selectedEmployee == null) {
            ExceptionHandler.showAlert("Please select an employee.");
        } else {
            ExceptionHandler.showAlert("Please select a team.");
        }
    }


    public void loadEmployeesOfTeam(int teamId) {
        List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(teamId);
        ObservableList<Employees> observableList = FXCollections.observableArrayList(employeesOfTeam);
        employeesOfTeamList.setAll(observableList);

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

    // method to calculate employee hourly rate and save it
    public double calculateEmployeeHourlyRate(Employees employee) {
        double annualCost = employee.getSalary() + employee.getConfigurableAmount();
        double totalCost = annualCost + (annualCost * (employee.getMultiplier() / 100)) + employee.getOverheadCost();
        double hourlyRate = totalCost / employee.getWorkingHours();

        return hourlyRate;
    }

    // method to calculate total hourly rate per team
    public double calculateTotalHourlyRateForTeam(List<Employees> employeesOfTeam) {
        double totalHourlyRate = 0;
        for (Employees employee : employeesOfTeam) {
            totalHourlyRate += calculateEmployeeHourlyRate(employee);
        }
        return totalHourlyRate;
    }

    @FXML
    void addEmployeePopUp(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addEmployee.fxml"));
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
            ExceptionHandler.showAlert("Operation Failed");
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
            ExceptionHandler.showAlert("Operation Failed");
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
                ExceptionHandler.showAlert("Operation Failed");
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
            ExceptionHandler.showAlert("Operation Failed");
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
                ExceptionHandler.showAlert("Operation Failed");
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

//    private void addMultipliers(Teams selectedTeam) {
//        // Retrieve markup and GM multipliers from text fields
//        double markupMultiplier = Double.parseDouble(markupMultiplierTxtField.getText());
//        double gmMultiplier = Double.parseDouble(gmMultiplierTxtField.getText());
//
//        if (selectedTeam != null) {
//            // Recalculate hourly rate with UP percentage
//            double upPercentage = utilizationPController.getUpPercentage();
//            double hourlyRateWithUP = calculateHourlyRateWithUP(selectedTeam.getId(), upPercentage);
//
//            // Apply multipliers to the hourly rate with UP
//            double hourlyRateWithMultipliers = hourlyRateWithUP * (1 + (markupMultiplier / 100)) * (1 + (gmMultiplier / 100));
//
//            // Update the hourly rate with multipliers in the selected team
//            updateHourlyRateWithMultipliers(selectedTeam, hourlyRateWithMultipliers);
//
//            // Refresh the teams table
//            //loadTeams();
//        }
//