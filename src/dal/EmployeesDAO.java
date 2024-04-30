package dal;

import be.Employees;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.IEmployeesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeesDAO implements IEmployeesDAO {

    private PreparedStatement preparedStatement;
    private DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

    @Override
    public List<Employees> getAllEmployees() {
        List<Employees> Employees = new ArrayList<>();
        try {
            String sql = "SELECT * FROM employees";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employees.add(new Employees(
                        resultSet.getInt("id"),
                        resultSet.getString("employeeName"),
                        resultSet.getInt("salary"),
                        resultSet.getInt("multiplier"),
                        resultSet.getInt("configurableAmount"),
                        resultSet.getString("country"),
                        resultSet.getString("team"),
                        resultSet.getInt("workingHours"),
                        resultSet.getInt("utilizationPercentage"),
                        resultSet.getInt("overheadCost")
                ));
            }
            return Employees;

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEmployee(Employees employees){
        try {
            String sql = "INSERT INTO employees(employeeName, salary, multiplier, configurableAmount, country, team, workingHours, utilizationPercentage, overheadCost) VALUES(?,?,?,?,?,?,?,?,?)";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, employees.getEmployeeName());
            preparedStatement.setDouble(2, employees.getSalary());
            preparedStatement.setDouble(3, employees.getMultiplier());
            preparedStatement.setDouble(4, employees.getConfigurableAmount());
            preparedStatement.setString(5, employees.getCountry());
            preparedStatement.setString(6, employees.getTeam());
            preparedStatement.setDouble(7, employees.getWorkingHours());
            preparedStatement.setDouble(8, employees.getUtilizationPercentage());
            preparedStatement.setDouble(9, employees.getOverheadCost());

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(Employees employees) {
        try {
            String sql = "UPDATE employees SET employeeName = ?, salary = ?, multiplier = ?, configurableAmount = ?, country = ?, team = ?, workingHours = ?, utilizationPercentage = ?, overheadCost = ?)";
            Connection conn = preparedStatement.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, employees.getEmployeeName());
            preparedStatement.setDouble(2, employees.getSalary());
            preparedStatement.setDouble(3, employees.getMultiplier());
            preparedStatement.setDouble(4, employees.getConfigurableAmount());
            preparedStatement.setString(5, employees.getCountry());
            preparedStatement.setString(6, employees.getTeam());
            preparedStatement.setDouble(7, employees.getWorkingHours());
            preparedStatement.setDouble(8, employees.getUtilizationPercentage());
            preparedStatement.setDouble(9, employees.getOverheadCost());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
