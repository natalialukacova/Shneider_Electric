package gui.search;

import be.Teams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

import java.util.List;

public class TeamSearch {

    private ObservableList<Teams> teamsList;
    private FilteredList<Teams> filteredList;

    public TeamSearch() {
        this.teamsList = FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(teamsList, p -> true);
    }

    public void setTeamsList(List<Teams> teams) {
        teamsList.setAll(teams);
    }

    public void setSearchCriteria(String searchText) {
        filteredList.setPredicate(teams -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchText.toLowerCase();
            if (teams.getTeamName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });
    }

    public SortedList<Teams> getSortedList() {
        return new SortedList<>(filteredList);
    }

    public void bindToTeamsTable(TableView<Teams> tableView) {
        tableView.setItems(getSortedList());
    }
}
