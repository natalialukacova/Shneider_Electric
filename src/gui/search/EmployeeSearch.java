package gui.search;

import be.Employees;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

import java.util.List;

public class EmployeeSearch {
    private ObservableList<Employees> employeesList;
    private FilteredList<Employees> filteredList;

    public EmployeeSearch() {
        this.employeesList = FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(employeesList, p -> true);
    }

    public void setEmployeesList(List<Employees> employees) {
        employeesList.setAll(employees);
    }

    public void setSearchCriteria(String searchText) {
        filteredList.setPredicate(employee -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchText.toLowerCase();
            if (employee.getEmployeeName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (employee.getGeography().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });
    }

    public SortedList<Employees> getSortedList() {
        return new SortedList<>(filteredList);
    }

    public void bindToEmployeesTable(TableView<Employees> tableView) {
        tableView.setItems(getSortedList());
    }
}
