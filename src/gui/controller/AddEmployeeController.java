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

public class AddEmployeeController extends MainViewController {

    @FXML
    private TableView<Employees> employeesTableView;
    @FXML
    public TableColumn<Employees, String> nameColumn;
    @FXML
    public TableColumn<Employees, Double> hourlyRateColumn;
    @FXML
    public TextField nameTxtField, salaryTxtField, multiplierTxtField, configurableAmountTxtField, workingHoursTxtField, utilizationPercentageTxtField, overheadCostTxtField;
    private EmployeesDAO employeesDAO;
    private MainViewController mainController;
    private Stage stage;
    private Employees selectedEmployee;


    public void initialize() {
        employeesDAO = new EmployeesDAO();

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

    public void confirmAddEmployee (ActionEvent event) {
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

        employeesDAO.addEmployee(newEmployee);
        mainController.loadAllEmployees();
        stage.close();
    }

    // display information of selected employee
    public void updateEmployee(ActionEvent event) {
        selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            fillEmployeeData(selectedEmployee);
        } else {
            showAlert("Please select an employee.");
        }
    }

    public void saveEditedEmployee(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

       /* selectedEmployee.setEmployeeName(nameTxtField.getText());
        selectedEmployee.setSalary(Double.parseDouble(salaryTxtField.getText()));
        selectedEmployee.setMultiplier(Double.parseDouble(multiplierTxtField.getText()));
        selectedEmployee.setConfigurableAmount(Double.parseDouble(configurableAmountTxtField.getText()));
        selectedEmployee.setWorkingHours(Double.parseDouble(workingHoursTxtField.getText()));
        selectedEmployee.setUtilizationPercentage(Double.parseDouble(utilizationPercentageTxtField.getText()));
        selectedEmployee.setOverheadCost(Double.parseDouble(overheadCostTxtField.getText()));
*/
        employeesDAO.updateEmployee(selectedEmployee);
        mainController.loadAllEmployees();

        stage.close();
    }

    public Employees fillEmployeeData(Employees employee) {
        this.selectedEmployee = employee;
        nameTxtField.setText(selectedEmployee.getEmployeeName());
        salaryTxtField.setText(String.valueOf(selectedEmployee.getSalary()));
        multiplierTxtField.setText(String.valueOf(selectedEmployee.getMultiplier()));
        configurableAmountTxtField.setText(String.valueOf(selectedEmployee.getConfigurableAmount()));
        workingHoursTxtField.setText(String.valueOf(selectedEmployee.getWorkingHours()));
        utilizationPercentageTxtField.setText(String.valueOf(selectedEmployee.getUtilizationPercentage()));
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
        utilizationPercentageTxtField.clear();
        overheadCostTxtField.clear();
    }

    public void setSelectedEmployee(Employees selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
        fillEmployeeData(selectedEmployee);
    }

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
