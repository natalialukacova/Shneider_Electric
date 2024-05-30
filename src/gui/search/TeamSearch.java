package gui.search;

import be.Teams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


public class TeamSearch {

    public static void search(TableView<Teams> teamsTable, ObservableList<Teams> allTeams, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            teamsTable.setItems(allTeams);
        } else {
            ObservableList<Teams> filteredTeams = FXCollections.observableArrayList();
            String lowerCaseQuery = searchText.toLowerCase();

            for (Teams team : allTeams) {
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
