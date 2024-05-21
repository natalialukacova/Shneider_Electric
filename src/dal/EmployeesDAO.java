package dal;

import be.Employees;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.IEmployeesDAO;

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
                        resultSet.getDouble("salary"),
                        resultSet.getDouble("multiplier"),
                        resultSet.getDouble("configurableAmount"),
                        resultSet.getDouble("workingHours"),
                        resultSet.getDouble("overheadCost"),
                        resultSet.getString("geography")
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
            String sql = "INSERT INTO employees(employeeName, salary, multiplier, configurableAmount, workingHours, utilizationPercentage, overheadCost, geography, hourlyRate) VALUES(?,?,?,?,?,?,?,?,?)";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            //preparedStatement.setInt(1, employees.getId());
            preparedStatement.setString(1, employees.getEmployeeName());
            preparedStatement.setDouble(2, employees.getSalary());
            preparedStatement.setDouble(3, employees.getMultiplier());
            preparedStatement.setDouble(4, employees.getConfigurableAmount());
            preparedStatement.setDouble(5, employees.getWorkingHours());
            preparedStatement.setDouble(6, employees.getUtilizationPercentage());
            preparedStatement.setDouble(7, employees.getOverheadCost());
            preparedStatement.setString(8, employees.getGeography());
            preparedStatement.setDouble(9, employees.getHourlyRate());


            preparedStatement.execute();

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(Employees employees) {
        if (employees == null) {
            throw new IllegalArgumentException("Employee object cannot be null.");
        }
        try {
            String sql = "UPDATE employees SET employeeName = ?, salary = ?, multiplier = ?, configurableAmount = ?, workingHours = ?, overheadCost = ?, geography = ? hourlyRate = ? WHERE id = ?";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, employees.getEmployeeName());
            preparedStatement.setDouble(2, employees.getSalary());
            preparedStatement.setDouble(3, employees.getMultiplier());
            preparedStatement.setDouble(4, employees.getConfigurableAmount());
            preparedStatement.setDouble(5, employees.getWorkingHours());
            preparedStatement.setDouble(6, employees.getOverheadCost());
            preparedStatement.setString(7, employees.getGeography());
            preparedStatement.setDouble(8, employees.getHourlyRate());
            preparedStatement.setInt(9, employees.getId());

            preparedStatement.executeUpdate();

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUtilizationPercentage(int employeeId, double utilizationPercentage) {
        try {
            String sql = "UPDATE employees SET utilizationPercentage = ? WHERE id = ?";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);

            preparedStatement.setDouble(1, utilizationPercentage);
            preparedStatement.setInt(2, employeeId);

            preparedStatement.executeUpdate();

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEmployeeHourlyRate(double hourlyRate) {
        try {
            String sql = "INSERT INTO employees (hourlyRate) VALUES(?) ";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);
            preparedStatement.setDouble(1, hourlyRate);
          //  preparedStatement.setInt(2, employeeId);

            preparedStatement.executeUpdate();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
