package dal.interfaces;

import be.Employees;

import java.util.List;

public interface IEmployeesTeamsDAO {
    List<Employees>getEmployeesOfTeam(int teamId);

   // void addEmployeeToTeam(int teamId, int employeeId, double up);

    boolean isEmployeeInTeam(int teamId, int employeeId);
}
