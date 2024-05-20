package gui.controller.employee;

import be.Employees;
import dal.EmployeesDAO;
import gui.controller.MainViewController;
import gui.utility.ExeptionHandeler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditEmployeeController {
    @FXML
    public TextField nameTxtField, salaryTxtField, multiplierTxtField, configurableAmountTxtField, workingHoursTxtField, utilizationPercentageTxtField, overheadCostTxtField, geographyTxtField;
    @FXML
    private ComboBox countryComboBox;
    private EmployeesDAO employeesDAO = new EmployeesDAO();
    private MainViewController mainController;
    private Stage stage;
    private Employees selectedEmployee;

    public void initialize() {
        employeesDAO = new EmployeesDAO();
    }

    public void confirmEditEmployee(ActionEvent event) {
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
        String geography = geographyTxtField.getText();

        Employees newEmployee = new Employees(0, employeeName, salary, multiplier, configurableAmount, workingHours, utilizationPercentage, overheadCost, geography);

        employeesDAO.updateEmployee(newEmployee);
        mainController.loadAllEmployees();
        saveEditedEmployee(event);
        stage.close();
    }

    private boolean validateInput() {
        if (nameTxtField.getText()==null || nameTxtField.getText().isEmpty() || geographyTxtField.getText()==null || geographyTxtField.getText().isEmpty()) {
            ExeptionHandeler.showAlert("Please fill in all required fields.");
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

    public void saveEditedEmployee(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        selectedEmployee.setEmployeeName(nameTxtField.getText());
        selectedEmployee.setSalary(Double.parseDouble(salaryTxtField.getText()));
        selectedEmployee.setMultiplier(Double.parseDouble(multiplierTxtField.getText()));
        selectedEmployee.setConfigurableAmount(Double.parseDouble(configurableAmountTxtField.getText()));
        selectedEmployee.setWorkingHours(Double.parseDouble(workingHoursTxtField.getText()));
        selectedEmployee.setUtilizationPercentage(Double.parseDouble(utilizationPercentageTxtField.getText()));
        selectedEmployee.setOverheadCost(Double.parseDouble(overheadCostTxtField.getText()));
        selectedEmployee.setGeography(geographyTxtField.getText());

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
        geographyTxtField.setText(selectedEmployee.getGeography());

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

    public Employees getSelectedEmployee(){
        return selectedEmployee;
    }

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }

    public void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void cancelBtn(ActionEvent event) {
        closeWindow(event);
    }
}
