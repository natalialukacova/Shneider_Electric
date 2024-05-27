package gui.controller;

import be.Countries;
import be.Employees;
import be.Teams;
import bll.TeamManager;
import dal.CountriesDAO;
import dal.EmployeesDAO;
import dal.EmployeesTeamsDAO;
import dal.TeamsDAO;
import bll.EmployeeManager;
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
    private final CountriesDAO countriesDAO = new CountriesDAO();
    private final TeamsDAO teamsDAO = new TeamsDAO();
    @FXML
    public TableView<Teams> teamsTableView;
    @FXML
    private TableColumn<Teams, String> teamNameCol;
    @FXML
    private TableColumn<Teams, String> teamCountryCol;
    @FXML
    public TableColumn<Teams, Double> hourlyRateColumn;
    @FXML
    private TableColumn<Teams, Double> addedMultipliersColumn;

    @FXML
    private TableColumn<Teams, Double> addedUpColumn;

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
    private Button addMultiplierBtn;
    private EditEmployeeController editEmployeeController;
    private ObservableList<Employees> employeesOfTeamList = FXCollections.observableArrayList();
    private Teams selectedTeam;
    private TeamManager teamManager;
    private  EmployeeManager employeeManager;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private UtilizationPController utilizationPController;

    private TextField upTxtField;

    public void setDependencies(EditEmployeeController editEmployeeController) {
        this.editEmployeeController = editEmployeeController;
    }

    public void setEmployeesTeamsDAO(EmployeesTeamsDAO employeesTeamsDAO) {
        this.employeesTeamsDAO = employeesTeamsDAO;
    }

    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    // Getter for employeesTeamsDAO
    public EmployeesTeamsDAO getEmployeesTeamsDAO() {
        return employeesTeamsDAO;
    }

    public void initialize() {

        upTxtField = new TextField();
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
        setTeamsTable(teamsTableView);
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
            loadEmployeesOfTeam(selectedTeam.getId());
            updateTeamHourlyRateInView(selectedTeam); // Refresh the view
            }
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

        // Setup DecimalFormatter for addedUpColumn
//        addedUpColumn.setCellFactory(tc -> new TableCell<>() {
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(decimalFormat.format(item));
//                }
//            }
//        });

    }

    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        employeesTableView.getItems().setAll(employeesDAO.getAllEmployees());
        employeesTableView.getColumns().clear();

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        countryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGeography()));

        employeesTableView.getColumns().addAll(nameColumn, countryColumn);

        /*employeeSearch.setEmployeesList(employees);
        employeeSearch.bindToEmployeesTable(employeesTableView);*/
    }

    // Calculate total hourly rate per team with given UP% obtained from a text field
    public double calculateTotalHourlyRateForTeam(List<Employees> employeesOfTeam, double upPercentage) {
        double totalHourlyRate = 0;
        for (Employees employee : employeesOfTeam) {
            totalHourlyRate += employee.getHourlyRate();
        }
        // Apply UP%
        return totalHourlyRate * (1 + (upPercentage / 100));
    }


    public void setTeamsTable(TableView<Teams> teamsTableView) {
        if (teamsTableView == null) {
            System.out.println("teamsTableView is null");
            return;
        }

        teamsTableView.getItems().setAll(teamsDAO.getAllTeams());

        teamsTableView.getColumns().clear();

        teamNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamName()));
        teamCountryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountryName()));

        hourlyRateColumn.setCellValueFactory(cellData -> {
            double upPercentage;
            try {
                upPercentage = Double.parseDouble(upTxtField.getText());
            } catch (NumberFormatException e) {
                // Handle the case where the text field is empty or contains non-numeric characters
                // For example, you could set a default value or display an error message
                upPercentage = 0.0; // Set a default value
            }

            // Get employees of the team
            List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(cellData.getValue().getId());
            // Calculate average hourly rate per team
            double totalHourlyRate = calculateTotalHourlyRateForTeam(employeesOfTeam, upPercentage);
            return new SimpleDoubleProperty(totalHourlyRate).asObject();
        });

        addedUpColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Teams) {
                Teams team = cellData.getValue();
                double upPercentage;
                try {
                    upPercentage = Double.parseDouble(upTxtField.getText());
                } catch (NumberFormatException e) {
                    // Handle the case where the text field is empty or contains non-numeric characters
                    // For example, you could set a default value or display an error message
                    upPercentage = 0.0; // Set a default value
                }

                // Calculate hourly rate with added UP%
                double hourlyRateWithUp = calculateTotalHourlyRateForTeam(employeesTeamsDAO.getEmployeesOfTeam(team.getId()), upPercentage);
                return new SimpleDoubleProperty(hourlyRateWithUp).asObject();
            } else {
                return new SimpleDoubleProperty(0).asObject(); // Return a default value if not Teams instance
            }
        });





        teamsTableView.getColumns().addAll(teamNameCol, teamCountryCol, hourlyRateColumn);
        /*List<Teams> filtered = teamSearch.search(teamsOfCountry, )
        teamSearch.setTeamsList(teamsOfCountry);
        teamSearch.bindToTeamsTable(teamsTableView);*/
    }

    public void updateTeamHourlyRateInView(Teams team) {
        teamsTableView.refresh();
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

                // Pass the value of upTxtField to the UtilizationPController
                utilizationPController.initializeUpTxtField(upTxtField.getText());

                // Retrieve the UP percentage from upTxtField and pass it to the calculation method
                double upPercentage = Double.parseDouble(utilizationPController.upTxtField.getText());
                // Retrieve employees of the selected team
                List<Employees> employeesOfTeam = employeesTeamsDAO.getEmployeesOfTeam(selectedTeam.getId());
                double hourlyRateWithUP = calculateTotalHourlyRateForTeam(employeesOfTeam, upPercentage);
                utilizationPController.setHourlyRateWithUP(hourlyRateWithUP);

                addMultiplierBtn.setDisable(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectedEmployee == null) {
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
            setTeamsTable(teamsTableView);
        }
    }

    private void setupEmployeeSearchField() {
        employeeSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            employeeSearch.setSearchCriteria(newValue);
        });
    }

    private void setupTeamSearchField() {
        teamSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Teams> filtered = teamSearch.search(teamsDAO.getAllTeams(), newValue);
            teamsTableView.getItems().setAll(filtered);
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
