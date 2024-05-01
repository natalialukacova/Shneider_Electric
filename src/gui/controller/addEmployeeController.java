package gui.controller;

import be.Employees;
import dal.EmployeesDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class addEmployeeController {

    @FXML
    private TableView<Employees> employeesTableView;
    @FXML
    public TableColumn<Employees, String> nameColumn;
    @FXML
    public TableColumn<Employees, Double> hourlyRateColumn;
    @FXML
    private TextField nameTxtField, salaryTxtField, multiplierTxtField, configurableAmountTxtField, workingHoursTxtField, utilizationPercentageTxtField, overheadCostTxtField;
    private EmployeesDAO employeesDAO;


    public void initialize() {
        employeesDAO = new EmployeesDAO();
        setEmployeesTable(employeesTableView);
        loadAllEmployees();
    }

    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        EmployeesDAO employeesDAO = new EmployeesDAO();
        List<Employees> data = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableData = FXCollections.observableArrayList(data);
        employeesTableView.setItems(observableData);

        // Clear existing columns
        employeesTableView.getColumns().clear();

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));
        hourlyRateColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalary()).asObject());

        // Add columns to the table view
        employeesTableView.getColumns().addAll(nameColumn, hourlyRateColumn);

    }

    private boolean validateInput() {
        if (nameTxtField.getText()==null || nameTxtField.getText().isEmpty()) {
            showAlert("Please fill in all required fields.");
            System.out.println("employee is null");
            return false;
        }
        try{
            Double.parseDouble(salaryTxtField.getText());
            Double.parseDouble(multiplierTxtField.getText());
            Double.parseDouble(configurableAmountTxtField.getText());
            Double.parseDouble(workingHoursTxtField.getText());
            Double.parseDouble(utilizationPercentageTxtField.getText());
            Double.parseDouble(overheadCostTxtField.getText());
        } catch (NumberFormatException e) {
            showAlert("Please enter valid numbers for numeric fields.");
            return false;
        }
        return true;
    }

    public void addEmployee (ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        String employeeName = nameTxtField.getText();
        Double salary = Double.parseDouble(salaryTxtField.getText());
        Double multiplier = Double.parseDouble(multiplierTxtField.getText());
        Double configurableAmount = Double.parseDouble(configurableAmountTxtField.getText());
        Double workingHours = Double.parseDouble(workingHoursTxtField.getText());
        Double utilizationPercentage = Double.parseDouble(utilizationPercentageTxtField.getText());
        Double overheadCost = Double.parseDouble(overheadCostTxtField.getText());

        Employees newEmployee = new Employees(0, employeeName, salary, multiplier, configurableAmount, workingHours, utilizationPercentage, overheadCost);

        EmployeesDAO employeeDAO = new EmployeesDAO();
        employeeDAO.addEmployee(newEmployee);
        loadAllEmployees();
        clearInputFields(event);
    }

    // display information of selected employee
    public void updateEmployee(ActionEvent event) {
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            fillEmployeeData(selectedEmployee);
        } else {
            showAlert("Please select an employee.");
        }
    }

    private Employees fillEmployeeData(Employees employee) {
        nameTxtField.setText(employee.getEmployeeName());
        salaryTxtField.setText(String.valueOf(employee.getSalary()));
        multiplierTxtField.setText(String.valueOf(employee.getMultiplier()));
        configurableAmountTxtField.setText(String.valueOf(employee.getConfigurableAmount()));
        workingHoursTxtField.setText(String.valueOf(employee.getWorkingHours()));
        utilizationPercentageTxtField.setText(String.valueOf(employee.getUtilizationPercentage()));
        overheadCostTxtField.setText(String.valueOf(employee.getOverheadCost()));

        return employee;
    }

    public void saveEditedEmployee(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {

            selectedEmployee().setEmployeeName(nameTxtField.getText());
            selectedEmployee().setSalary(Double.parseDouble(salaryTxtField.getText()));
            selectedEmployee().setMultiplier(Double.parseDouble(multiplierTxtField.getText()));
            selectedEmployee().setConfigurableAmount(Double.parseDouble(configurableAmountTxtField.getText()));
            selectedEmployee().setWorkingHours(Double.parseDouble(workingHoursTxtField.getText()));
            selectedEmployee().setUtilizationPercentage(Double.parseDouble(utilizationPercentageTxtField.getText()));
            selectedEmployee().setOverheadCost(Double.parseDouble(overheadCostTxtField.getText()));

            employeesDAO.updateEmployee(selectedEmployee);
            employeesTableView.refresh();
            clearInputFields(event);
        } else {
            showAlert("No employee selected for update.");
        }
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
        utilizationPercentageTxtField.clear();
        overheadCostTxtField.clear();
    }

    public void loadAllEmployees() {
        EmployeesDAO employeeDAO = new EmployeesDAO();
        List<Employees> allEmployees = employeeDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(allEmployees);
        employeesTableView.getItems().setAll(allEmployees);
    }

    private Employees selectedEmployee(){
        return employeesTableView.getSelectionModel().getSelectedItem();
    }

    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
