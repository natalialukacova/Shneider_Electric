package dal;

import be.Teams;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.ITeamsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamsDAO implements ITeamsDAO {
    private PreparedStatement preparedStatement;
    private DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

    @Override
    public List<Teams> getAllTeams() {
        List<Teams> Teams = new ArrayList<>();
        try {
            String sql = "SELECT * FROM teams";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Teams.add(new Teams(
                        resultSet.getInt("teamId"),
                        resultSet.getString("teamName")
                ));
            }
            return Teams;

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTeam(Teams teams) {
        try {
            String sql = "INSERT INTO teams values(?)";
            Connection conn = databaseConnector.getConnection();
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, teams.getTeamName());
            preparedStatement.execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTeam(Teams teams) {
        try {
            String sql = "UPDATE teams SET teamName = ? WHERE teamId = ?";
            Connection conn = databaseConnector.getConnection();
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, teams.getTeamName());
            preparedStatement.executeUpdate();

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
