package dal;

import be.Employees;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.IEmployeesTeamsDAO;
import gui.controller.MainViewController;
import gui.utility.ExceptionHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeesTeamsDAO implements IEmployeesTeamsDAO {
    private PreparedStatement preparedStatement;
    private DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
    private MainViewController mainViewController;



    @Override
    public List<Employees> getEmployeesOfTeam(int teamId) {
        List<Employees> EmployeesOfTeam = new ArrayList<>();
        try {
            String sql = "SELECT * FROM employees INNER JOIN employeesTeam ON employees.id = employeesTeam.employeeId "
                    + "INNER JOIN teams ON employeesTeam.teamId = teams.id WHERE employeesTeam.teamId = ?";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, teamId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EmployeesOfTeam.add(new Employees(
                        resultSet.getInt("id"),
                        resultSet.getString("employeeName"),
                        resultSet.getDouble("salary"),
                        resultSet.getDouble("multiplier"),
                        resultSet.getDouble("configurableAmount"),
                        resultSet.getDouble("workingHours"),
                        resultSet.getDouble("overheadCost"),
                        resultSet.getString("geography")
                ));
            }
            return EmployeesOfTeam;
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEmployeeToTeam(int teamId, int employeeId, double up) {
        if (!isEmployeeInTeam(teamId, employeeId)) {
            try {
                String sql = "INSERT INTO [employeesTeam] (teamId, employeeId, up) VALUES (?,?,?)";
                preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

                preparedStatement.setInt(1, teamId);
                preparedStatement.setInt(2, employeeId);
                preparedStatement.setDouble(3, up);
                preparedStatement.executeUpdate();
            } catch (SQLServerException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            ExceptionHandler.showAlert("Employee is already part of the team.");
        }
    }

    private boolean isEmployeeInTeam(int teamId, int employeeId) {
        try {
            String sql = "SELECT COUNT(*) FROM employeesTeam WHERE teamId = ? AND employeeId = ?";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}


