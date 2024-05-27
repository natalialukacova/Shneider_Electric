package gui.search;

import be.Employees;
import be.Teams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

import java.util.List;

public class EmployeeTeamSearch {

    public static void search(TableView<Employees> employeesOfTeamTable, ObservableList<Employees> allEmployeesOfTeam, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            employeesOfTeamTable.setItems(allEmployeesOfTeam);
        } else {
            ObservableList<Employees> filteredEmployees = FXCollections.observableArrayList();
            String lowerCaseQuery = searchText.toLowerCase();

            for (Employees employee : allEmployeesOfTeam) {
                if (employee.getEmployeeName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredEmployees.add(employee);
                }
            }
            employeesOfTeamTable.setItems(filteredEmployees);
        }
    }
}
