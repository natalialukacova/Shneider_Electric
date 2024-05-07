package dal;

import be.Teams;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.ITeamsCountriesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamsCountriesDAO implements ITeamsCountriesDAO {

    private PreparedStatement preparedStatement;
    private DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

    @Override
    public List<Teams> getAllTeamsOfCountry(){
        List<Teams> TeamsInCountry = new ArrayList<>();
        try {
            String sql = "SELECT * FROM teamsCountry";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                TeamsInCountry.add(new Teams(
                        resultSet.getInt("id"),
                        resultSet.getInt("countryId"),
                        resultSet.getInt("teamId")
                ));
            }
            return TeamsInCountry;

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addTeamInCountry(Teams teams) {
        try {
            String sql = "INSERT INTO teamsCountry (teamId, countryId) VALUES (?, ?)";
            Connection conn = databaseConnector.getConnection();
            preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, teams.getTeamId());
            preparedStatement.setInt(2, teams.getCountryId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key constraint violation")) {
                throw new RuntimeException("Error adding team: Country with ID " + teams.getCountryId() + " not found.", e);
            } else {
                throw new RuntimeException("Error while adding team.", e);
            }
        }
    }

}
