package dal.interfaces;

import be.Employees;

import java.util.List;

public interface IEmployeesDAO {
    List<Employees> getAllEmployees();
    void addEmployee(Employees employees);
    void updateEmployee(Employees employees);

    void updateUtilizationPercentage(int employeeId, double utilizationPercentage);
}
