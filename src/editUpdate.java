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
import java.util.Objects;

public class editUpdate {

    private TableView<Employees> employeesTableView;
    @FXML
    public TextField nameTxtField;
    @FXML
    public TextField salaryTxtField;
    @FXML
    public TextField multiplierTxtField;
    @FXML
    public TextField configurableAmountTxtField;
    @FXML
    public TextField countryTxtField;
    @FXML
    public TextField teamTxtField;
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

        // Define columns using a loop
        String[] columnProperties = {"employeeName", "salary", "multiplier", "configurableAmount", "country", "team", "workingHours", "utilizationPercentage", "overheadCost"};
        String[] columnHeaders = {"Name", "Salary", "Multiplier", "Configurable Amount", "Country", "Team", "Working Hours", "Utilization Percentage", "Overhead Cost"};
        for (int i = 0; i < columnProperties.length; i++) {
            final int columnIndex = i;
            TableColumn<Employees, String> column = new TableColumn<>(columnHeaders[i]);
            column.setCellValueFactory(cellData -> {
                try {
                    Object value = cellData.getValue().getClass().getMethod("get" + columnProperties[columnIndex].substring(0, 1).toUpperCase() + columnProperties[columnIndex].substring(1)).invoke(cellData.getValue());
                    return new SimpleStringProperty(value.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("");
                }
            });
            employeesTableView.getColumns().add(column);
        }

    }

    public void addEmployee(ActionEvent event) {
        String employeeName = nameTxtField.getText();
        double salary = Double.parseDouble(salaryTxtField.getText());
        double multiplier = Double.parseDouble(multiplierTxtField.getText());
        double configurableAmount = Double.parseDouble(configurableAmountTxtField.getText());
        String country = countryTxtField.getText();
        String team = teamTxtField.getText();
        double workingHours = Double.parseDouble(workingHoursTxtField.getText());
        double utilizationPercentage = Double.parseDouble(utilizationPercentageTxtField.getText());
        double overheadCost = Double.parseDouble(overheadCostTxtField.getText());

        if (employeeName.isEmpty() || country.isEmpty() || team.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
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
            return;
        }

        Employees newEmployee = new Employees(0, employeeName, salary, multiplier, configurableAmount, country, team, workingHours, utilizationPercentage, overheadCost);

        EmployeesDAO employeeDAO = new EmployeesDAO();
        employeeDAO.addEmployee(newEmployee);

        setEmployeesTable();
        clearInputFields();
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
        countryTxtField.clear();
        teamTxtField.clear();
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
        countryTxtField.clear();
        teamTxtField.clear();
        workingHoursTxtField.clear();
        utilizationPercentageTxtField.clear();
        overheadCostTxtField.clear();
    }

    public void editSelectedEmployee() {
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Please select an employee to edit.");
            return;
        }

    }


}
