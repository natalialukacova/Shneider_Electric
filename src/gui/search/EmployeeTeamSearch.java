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
    private ObservableList<Employees> employeeTeamList;
    private FilteredList<Employees> filteredList;

    public EmployeeTeamSearch() {
        this.employeeTeamList = FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(employeeTeamList, p -> true);
    }

    public void setEmployeeTeamList(List<Employees> employeeTeam) {
        employeeTeamList.setAll(employeeTeam);
    }

    public void setSearchCriteria(String searchText) {
        filteredList.setPredicate(employeeTeam -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchText.toLowerCase();
            if (employeeTeam.getEmployeeName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });
    }

    public SortedList<Employees> getSortedList() {
        return new SortedList<>(filteredList);
    }

    public void bindToEmployeeTeamTable(TableView<Employees> tableView) {
        tableView.setItems(getSortedList());
    }
}
