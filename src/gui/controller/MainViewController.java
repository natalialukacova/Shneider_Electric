package gui.controller;

import be.Employees;
import dal.EmployeesDAO;
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

import java.io.IOException;
import java.util.List;

public class MainViewController {

    @FXML
    private Button addEmplooyeeBtn;

    @FXML
    private Button addTeamBtn;

    @FXML
    private Button closeWindow;
    @FXML
    private Button minimizeWindow;
    private EmployeesDAO employeesDAO;
    @FXML
    private TableView<Employees> employeesTableView;
    @FXML
    public TableColumn<Employees, String> nameColumn;
    @FXML
    public TableColumn<Employees, Double> hourlyRateColumn;



    public void initialize() {
        employeesDAO = new EmployeesDAO();
        setEmployeesTable(employeesTableView);
    }

    public void setEmployeesTable(TableView<Employees> employeesTableView) {
        List<Employees> data = employeesDAO.getAllEmployees();
        ObservableList<Employees> observableData = FXCollections.observableArrayList(data);
        employeesTableView.setItems(observableData);

        // Clear existing columns
        employeesTableView.getColumns().clear();

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));

        employeesTableView.getColumns().addAll(nameColumn, hourlyRateColumn);
    }



    @FXML
    void addEmplooyeePopUp(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addEmplooyee.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            AddEmployeeController addEmployeeController = loader.getController();
            addEmployeeController.setMainController(this);
            addEmployeeController.setStage(stage);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void editEmployeeBtn(ActionEvent event) {
        Employees selectedEmployee = employeesTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/addEmplooyee.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                AddEmployeeController addEmployeeController = loader.getController();
                addEmployeeController.setMainController(this);
                addEmployeeController.setStage(stage);
                addEmployeeController.setSelectedEmployee(selectedEmployee);

                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select an employee to edit.");
        }
    }

    public void loadAllEmployees() {
        EmployeesDAO employeeDAO = new EmployeesDAO();
        List<Employees> allEmployees = employeeDAO.getAllEmployees();
        ObservableList<Employees> observableList = FXCollections.observableArrayList(allEmployees);
        employeesTableView.getItems().setAll(allEmployees);
    }

    public void updateEmployeeList(ObservableList<Employees> updatedList) {
        employeesTableView.setItems(updatedList);
    }

    @FXML
    void addTeamPopUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/addTeam.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void minimizeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setIconified(true);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
