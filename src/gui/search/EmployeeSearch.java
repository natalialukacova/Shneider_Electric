package gui.search;

import be.Employees;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class EmployeeSearch {

    public static void search(TableView<Employees> employeesTable, ObservableList<Employees> allEmployees, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            employeesTable.setItems(allEmployees);
        } else {
            ObservableList<Employees> filteredEmployees = FXCollections.observableArrayList();
            String lowerCaseQuery = searchText.toLowerCase();

            for (Employees employee : allEmployees) {
                if (employee.getEmployeeName().toLowerCase().contains(lowerCaseQuery) ||
                        employee.getGeography().toLowerCase().contains(lowerCaseQuery)) {
                    filteredEmployees.add(employee);
                }
            }
            employeesTable.setItems(filteredEmployees);
        }
    }
}
