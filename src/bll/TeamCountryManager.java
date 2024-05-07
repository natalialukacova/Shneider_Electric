package bll;

import be.Teams;
import dal.TeamsCountriesDAO;
import dal.interfaces.ITeamsCountriesDAO;

import java.util.List;

public class TeamCountryManager implements ITeamsCountriesDAO {
    ITeamsCountriesDAO teamsCountriesDAO = new TeamsCountriesDAO();

    @Override
    public List<Teams> getAllTeamsOfCountry() {
        return teamsCountriesDAO.getAllTeamsOfCountry();
    }

    @Override
    public void addTeamInCountry(Teams teamsCountry) {
        teamsCountriesDAO.addTeamInCountry(teamsCountry);

    }
}
