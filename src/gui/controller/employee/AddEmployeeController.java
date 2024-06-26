package gui.controller.employee;

import be.Employees;
import dal.EmployeesDAO;
import gui.controller.MainViewController;
import gui.utility.ExceptionHandler;
import javafx.collections.ObservableList;
import bll.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddEmployeeController {

    @FXML
    private TextField nameTxtField, salaryTxtField, multiplierTxtField, configurableAmountTxtField, workingHoursTxtField, overheadCostTxtField, geographyTxtField;
    private EmployeesDAO employeesDAO;
    private MainViewController mainController;
    private final EmployeeManager employeeManager = new EmployeeManager();
    private Stage stage;
    private ObservableList<Employees> employees;


    public void initialize() {
        employeesDAO = new EmployeesDAO();
    }

    public void setEmployees(ObservableList<Employees> employees) {
        this.employees = employees;
    }

    /**
     * Ensures that required text fields are not null nor empty and that specific fields contain valid numeric values.
     *
     * @return true if all validations pass, false otherwise.
     */
    private boolean validateInput() {
        if (nameTxtField.getText()==null || nameTxtField.getText().isEmpty() ||
                geographyTxtField.getText()==null || geographyTxtField.getText().isEmpty()) {
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

    public void confirmAddEmployee (ActionEvent event) {
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

        Employees newEmployee = new Employees(0, employeeName, salary, multiplier, configurableAmount, workingHours, overheadCost, geography);

        // Calculate and set the hourly rate
        double hourlyRate = employeeManager.calculateEmployeeHourlyRate(newEmployee);
        newEmployee.setHourlyRate(hourlyRate);

        employeesDAO.addEmployee(newEmployee);
        employees.add(newEmployee);
        stage.close();
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
