package dal;

import be.Teams;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.ITeamsDAO;

import java.sql.*;
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
                        resultSet.getInt("id"),
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




    public void addTeam(Teams teams) {
        try {
            String sql = "INSERT INTO teams VALUES (?)";
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
            String sql = "UPDATE teams SET teamName = ? WHERE id = ?";
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

    @Override
    public void deleteTeam(int id){
        try {
            String sql = "DELETE FROM teams WHERE id=?";
            Connection conn = databaseConnector.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
