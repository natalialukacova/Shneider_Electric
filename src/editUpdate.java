import be.Employees;
import dal.EmployeesDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.TableColumn;
import java.awt.event.ActionEvent;
import java.util.List;

public class editUpdate {

    private TableView<Employees> employeesTableView;
    public String employeeName;
    @FXML
    public TextField nameTxtField;
    @FXML
    public TextField salaryTxtField;
    @FXML
    public TextField multiplierTxtField;
    @FXML
    public TextField configurableAmountTxtField;
    @FXML
    public TextField workingHoursTxtField;
    @FXML
    public TextField utilizationPercentageTxtField;
    @FXML
    public TextField overheadCostTxtField;

    public void initialize() {
        setEmployeesTable();
        loadAllEmployees();
    }

    public void setEmployeesTable() {
        EmployeesDAO employeesDAO = new EmployeesDAO();
        List<Employees> data = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableData = FXCollections.observableArrayList(data);
        employeesTableView.setItems(observableData);

        // Clear existing columns
        //employeesTableView.getColumns().clear();

        // Define columns for employee name and hourly rate
        TableColumn<Employees, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));

        TableColumn<Employees, Double> hourlyRateColumn = new TableColumn<>("Hourly Rate");
        hourlyRateColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalary()).asObject());

        // Add columns to the table view
        employeesTableView.getColumns().addAll(nameColumn, hourlyRateColumn);

    }

    public void addEmployee(ActionEvent event) {
        employeeName = nameTxtField.getText();
        double salary = Double.parseDouble(salaryTxtField.getText());
        double multiplier = Double.parseDouble(multiplierTxtField.getText());
        double configurableAmount = Double.parseDouble(configurableAmountTxtField.getText());

        double workingHours = Double.parseDouble(workingHoursTxtField.getText());
        double utilizationPercentage = Double.parseDouble(utilizationPercentageTxtField.getText());
        double overheadCost = Double.parseDouble(overheadCostTxtField.getText());

        Employees newEmployee = new Employees(0, employeeName, salary, multiplier, configurableAmount, workingHours, utilizationPercentage, overheadCost);

        EmployeesDAO employeeDAO = new EmployeesDAO();
        employeeDAO.addEmployee(newEmployee);

        setEmployeesTable();
        clearInputFields();
    }

    private boolean validateInput() {
        if (employeeName.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return false;
        }
        try {
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // clear input fields after adding an employee
    private void clearInputFields() {
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

    public void clearFields(ActionEvent event) {
        nameTxtField.clear();
        salaryTxtField.clear();
        multiplierTxtField.clear();
        configurableAmountTxtField.clear();

        workingHoursTxtField.clear();
        utilizationPercentageTxtField.clear();
        overheadCostTxtField.clear();
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

    public void editSelectedEmployee() {
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Please select an employee to edit.");
            return;
        }

        fillEmployeeData(selectedEmployee);
    }

    public void updateEmployee(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("No employee selected for update.");
            return;
        }

        try {
            Employees updatedEmployee = fillEmployeeData(selectedEmployee);
            updatedEmployee.setId(selectedEmployee.getId());  // Preserve the existing ID

            EmployeesDAO employeeDAO = new EmployeesDAO();
            employeeDAO.updateEmployee(updatedEmployee);

            setEmployeesTable();  // Refresh the table
            clearInputFields();
            showAlert("Employee updated successfully.");
        } catch (NumberFormatException e) {
            showAlert("Error parsing numeric fields. Please check your inputs.");
        }
    }


}
