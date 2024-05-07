package dal.interfaces;

import be.Teams;

import java.util.List;

public interface ITeamsCountriesDAO {
    List<Teams> getAllTeamsOfCountry();
    //void addTeamInCountry(TeamsCountry teamsCountry);

    void addTeamInCountry(Teams teams);
}
