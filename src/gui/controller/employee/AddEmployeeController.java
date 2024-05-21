package gui.controller.employee;

import be.Countries;
import be.Employees;
import dal.CountriesDAO;
import dal.EmployeesDAO;
import gui.controller.MainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddEmployeeController {

    @FXML
    private TableView<Employees> employeesTableView;
    @FXML
    public TableColumn<Employees, String> nameColumn;
    @FXML
    public TableColumn<Employees, Double> hourlyRateColumn;
    @FXML
    public TextField nameTxtField, salaryTxtField, multiplierTxtField, configurableAmountTxtField, workingHoursTxtField, overheadCostTxtField, geographyTxtField;
    @FXML
    private ComboBox countryBox;
    private EmployeesDAO employeesDAO = new EmployeesDAO();
    private MainViewController mainController = new MainViewController();
    private Stage stage;
    private Employees selectedEmployee;


    public void initialize() {
        employeesDAO = new EmployeesDAO();

    }


    private boolean validateInput() {
        if (nameTxtField.getText()==null || nameTxtField.getText().isEmpty() || geographyTxtField.getText()==null || geographyTxtField.getText().isEmpty()) {
            showAlert("Please fill in all required fields.");
            System.out.println("employee is null");
            return false;
        }
        try{
            Double.parseDouble(salaryTxtField.getText());
            Double.parseDouble(multiplierTxtField.getText());
            Double.parseDouble(configurableAmountTxtField.getText());
            Double.parseDouble(workingHoursTxtField.getText());
            Double.parseDouble(overheadCostTxtField.getText());
        } catch (NumberFormatException e) {
            showAlert("Please enter valid numbers for numeric fields.");
            return false;
        }
        return true;
    }

    public void confirmAddEmployee (ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        String employeeName = nameTxtField.getText();
        Double salary = Double.parseDouble(salaryTxtField.getText());
        Double multiplier = Double.parseDouble(multiplierTxtField.getText());
        Double configurableAmount = Double.parseDouble(configurableAmountTxtField.getText());
        Double workingHours = Double.parseDouble(workingHoursTxtField.getText());
        Double overheadCost = Double.parseDouble(overheadCostTxtField.getText());
        String geography = geographyTxtField.getText();

        Employees newEmployee = new Employees(0, employeeName, salary, multiplier, configurableAmount, workingHours, overheadCost, geography);

        Double hourlyRate = mainController.calculateEmployeeHourlyRate(newEmployee);
        newEmployee.setHourlyRate(hourlyRate);

        employeesDAO.addEmployee(newEmployee);
        mainController.loadAllEmployees();
        stage.close();
    }

    // method for calculating
    public double calculateHourlyRate(double salary, double configurableAmount, double workingHours, double utilizationPercentage, double multiplier) {
        // Calculate day rate
        double dayRate = (salary + configurableAmount) / (workingHours * (utilizationPercentage / 100)) * (1 + (multiplier / 100));
        // Calculate hourly rate
        return dayRate / workingHours;
    }


    public Employees fillEmployeeData(Employees employee) {
        this.selectedEmployee = employee;
        nameTxtField.setText(selectedEmployee.getEmployeeName());
        salaryTxtField.setText(String.valueOf(selectedEmployee.getSalary()));
        multiplierTxtField.setText(String.valueOf(selectedEmployee.getMultiplier()));
        configurableAmountTxtField.setText(String.valueOf(selectedEmployee.getConfigurableAmount()));
        workingHoursTxtField.setText(String.valueOf(selectedEmployee.getWorkingHours()));
        overheadCostTxtField.setText(String.valueOf(selectedEmployee.getOverheadCost()));

        return employee;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void clearInputFields(ActionEvent event) {
        nameTxtField.clear();
        salaryTxtField.clear();
        multiplierTxtField.clear();
        configurableAmountTxtField.clear();
        workingHoursTxtField.clear();
        overheadCostTxtField.clear();
    }

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }

    public void cancelBtn(ActionEvent event) {
        stage.close();
    }
}
