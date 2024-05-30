package gui.controller.employee;

import be.Employees;
import dal.EmployeesDAO;
import bll.EmployeeManager;
import gui.controller.MainViewController;
import gui.utility.ExceptionHandler;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditEmployeeController {
    @FXML
    private TextField nameTxtField, salaryTxtField, multiplierTxtField, configurableAmountTxtField, workingHoursTxtField, overheadCostTxtField, geographyTxtField;
    private EmployeesDAO employeesDAO;
    private MainViewController mainController;
    private Stage stage;
    private Employees selectedEmployee;
    private ObservableList<Employees> employees;
    private final EmployeeManager employeeManager = new EmployeeManager();


    public void initialize() {
        employeesDAO = new EmployeesDAO();
    }

    public void setEmployees(ObservableList<Employees> employees) {
        this.employees = employees;
    }

    public void confirmEditEmployee(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        String employeeName = nameTxtField.getText();
        double salary = Double.parseDouble(salaryTxtField.getText());
        double multiplier = Double.parseDouble(multiplierTxtField.getText());
        double configurableAmount = Double.parseDouble(configurableAmountTxtField.getText());
        double workingHours = Double.parseDouble(workingHoursTxtField.getText());
        double overheadCost = Double.parseDouble(overheadCostTxtField.getText());
        String geography = geographyTxtField.getText();

        updateSelectedEmployee(employeeName, salary, multiplier,configurableAmount, workingHours, overheadCost, geography);

        double hourlyRate = employeeManager.calculateEmployeeHourlyRate(selectedEmployee);
        selectedEmployee.setHourlyRate(hourlyRate);

        employeesDAO.updateEmployee(selectedEmployee);

        stage.close();
    }

    /**
     * Ensures that required text fields are not null nor empty and that specific fields contain valid numeric values.
     *
     * @return true if all validations pass, false otherwise.
     */
    private boolean validateInput() {
        if (nameTxtField.getText()==null || nameTxtField.getText().isEmpty() || geographyTxtField.getText()==null || geographyTxtField.getText().isEmpty()) {
            ExceptionHandler.showAlert("Please fill in all required fields.");
            return false;
        }
        try{
            Double.parseDouble(salaryTxtField.getText());
            Double.parseDouble(multiplierTxtField.getText());
            Double.parseDouble(configurableAmountTxtField.getText());
            Double.parseDouble(workingHoursTxtField.getText());
            Double.parseDouble(overheadCostTxtField.getText());
        } catch (NumberFormatException e) {
            ExceptionHandler.showAlert("Please enter valid numbers for numeric fields.");
            return false;
        }
        return true;
    }

    private void updateSelectedEmployee(String employeeName, double salary, double multiplier, double configurableAmount, double workingHours, double overheadCost, String geography) {
        selectedEmployee.setEmployeeName(employeeName);
        selectedEmployee.setSalary(salary);
        selectedEmployee.setMultiplier(multiplier);
        selectedEmployee.setConfigurableAmount(configurableAmount);
        selectedEmployee.setWorkingHours(workingHours);
        selectedEmployee.setOverheadCost(overheadCost);
        selectedEmployee.setGeography(geography);
    }

    private Employees fillEmployeeData(Employees employee) {
        this.selectedEmployee = employee;
        nameTxtField.setText(selectedEmployee.getEmployeeName());
        salaryTxtField.setText(String.valueOf(selectedEmployee.getSalary()));
        multiplierTxtField.setText(String.valueOf(selectedEmployee.getMultiplier()));
        configurableAmountTxtField.setText(String.valueOf(selectedEmployee.getConfigurableAmount()));
        workingHoursTxtField.setText(String.valueOf(selectedEmployee.getWorkingHours()));
        overheadCostTxtField.setText(String.valueOf(selectedEmployee.getOverheadCost()));
        geographyTxtField.setText(selectedEmployee.getGeography());

        return employee;
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
