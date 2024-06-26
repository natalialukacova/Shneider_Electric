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

    public void addTeam(Teams teams) {
        try {
            String sql = "INSERT INTO teams(teamName, countryId, countryName) VALUES (?,?,?)";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, teams.getTeamName());
            preparedStatement.setInt(2, teams.getCountryId());
            preparedStatement.setString(3, teams.getCountryName());

            preparedStatement.execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addMultipliers(int teamId, double markupMultiplier, double gmMultiplier) {
        try {
            String sql = "UPDATE teams SET markupMultiplier = ?, gmMultiplier = ? WHERE id = ?";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setDouble(1, markupMultiplier);
            preparedStatement.setDouble(2, gmMultiplier);
            preparedStatement.setInt(3, teamId);

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
            String employeesTeamSQL = "DELETE FROM employeesTeam WHERE teamId=?";
            String teamSQL = "DELETE FROM teams WHERE id=?";
            Connection conn = databaseConnector.getConnection();

            // delete from employeesTeam table
            PreparedStatement deleteEmployeesTeamsStmt = conn.prepareStatement(employeesTeamSQL);
            deleteEmployeesTeamsStmt.setInt(1, id);
            deleteEmployeesTeamsStmt.executeUpdate();

            // delete from teams table
            PreparedStatement deleteTeamStmt = conn.prepareStatement(teamSQL);
            deleteTeamStmt.setInt(1,id);
            deleteTeamStmt.executeUpdate();

            conn.commit();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Teams> getAllTeams() {
        List<Teams> teams = new ArrayList<>();
        try {
            String sql = "SELECT * FROM teams";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Teams team = new Teams(
                            resultSet.getInt("id"),
                            resultSet.getString("teamName"),
                            resultSet.getDouble("teamHourlyRate"),
                            resultSet.getInt("countryId"),
                            resultSet.getDouble("markupMultiplier"),
                            resultSet.getDouble("gmMultiplier"),
                            resultSet.getString("countryName"));
                    teams.add(team);
                }
            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teams;
    }
}

