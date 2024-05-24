package gui.search;

import be.Teams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.stream.Collectors;

public class TeamSearch {

    public List<Teams> search(List<Teams> teamsList, String searchText) {
        return teamsList.stream().filter(teams -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchText.toLowerCase();
            if (teams.getTeamName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

}
