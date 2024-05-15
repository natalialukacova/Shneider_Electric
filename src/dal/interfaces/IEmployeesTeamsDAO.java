package dal.interfaces;

import be.Employees;

import java.util.List;

public interface IEmployeesTeamsDAO {
    List<Employees>getEmployeesOfTeam(int teamId);
}
