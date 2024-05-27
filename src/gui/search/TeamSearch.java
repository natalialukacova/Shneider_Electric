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

    public static void search(TableView<Teams> teamsTable, ObservableList<Teams> allTeams, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            teamsTable.setItems(allTeams);
        } else {
            ObservableList<Teams> filteredTeams = FXCollections.observableArrayList();
            String lowerCaseQuery = searchText.toLowerCase();

            for (Teams team : allTeams) {
               /* if (team.getTeamName().toLowerCase().contains(lowerCaseQuery) ||
                    team.getCountryName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredTeams.add(team);
                }*/
                boolean name = team.getTeamName() != null && team.getTeamName().contains(lowerCaseQuery);
                boolean country = team.getCountryName() != null && team.getCountryName().toLowerCase().contains(lowerCaseQuery);

                if (name || country) {
                    filteredTeams.add(team);
                }
            }
            teamsTable.setItems(filteredTeams);
        }
    }

}
