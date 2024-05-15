package dal;

import be.Employees;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.IEmployeesTeamsDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeesTeamsDAO implements IEmployeesTeamsDAO {
    private PreparedStatement preparedStatement;
    private DatabaseConnector databaseConnector = DatabaseConnector.getInstance();


    @Override
    public List<Employees> getEmployeesOfTeam(int teamId) {
        List<Employees> EmployeesOfTeam = new ArrayList<>();
        try {
            String sql = "SELECT * FROM employees INNER JOIN employeesTeam ON employees.id = emyployeesTeam.employeeId"
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
                        resultSet.getDouble("utilizationPercentage"),
                        resultSet.getDouble("overheadCost"),
                        resultSet.getString("geography")));
            }
            return EmployeesOfTeam;
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


