package bll;

import be.Employees;
import dal.EmployeesDAO;
import dal.interfaces.IEmployeesDAO;

import java.util.List;

public class EmployeeManager implements IEmployeesDAO {

    IEmployeesDAO employeesDAO = new EmployeesDAO();
    @Override
    public List<Employees> getAllEmployees() {
        return employeesDAO.getAllEmployees();
    }

    @Override
    public void addEmployee(Employees employees) {
        employeesDAO.addEmployee(employees);
    }

    @Override
    public void updateEmployee(Employees employees) {
        employeesDAO.updateEmployee(employees);
    }

    public double calculateEmployeeHourlyRate(Employees employee) {
        // Step 1: Fetch Employee Details
        double salary = employee.getSalary();
        double configurableAmount = employee.getConfigurableAmount();
        double multiplier = employee.getMultiplier();
        double overheadCost = employee.getOverheadCost();
        double workingHours = employee.getWorkingHours();

        // Step 2: Calculate Annual Cost
        double annualCost = salary + configurableAmount;

        // Step 3: Calculate Total Cost
        double totalCost = annualCost + (annualCost * (multiplier / 100)) + overheadCost;

        // Step 4: Calculate Hourly Rate
        double hourlyRate = totalCost / workingHours;

        return hourlyRate;
    }

}
