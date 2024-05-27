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

}
