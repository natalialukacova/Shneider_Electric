package gui.controller;

import be.Employees;
import be.Teams;
import dal.EmployeesDAO;
import dal.TeamsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static javafx.beans.binding.Bindings.when;
import static org.junit.Assert.assertEquals;

public class MainViewControllerTest {

    private MainViewController mainViewController;
    private EmployeesDAO employeesDAO;
    private TeamsDAO teamsDAO;


    @Before
    public void setUp() {
        mainViewController = new MainViewController();
    }

    @Test
    public void testCalculateTotalHourlyRateForTeam() {
        // Create sample employees
        List<Employees> employees = new ArrayList<>();
        employees.add(new Employees(1, "Morten Morten", 44000, 30, 1800, 1500, 1200, "UK"));
        employees.add(new Employees(2, "Lanita Rasmus", 22000, 20, 2000, 3500, 1800, "UK"));

        double totalHourlyRate = mainViewController.calculateTotalHourlyRateForTeam(employees);

        assertEquals(49.23619047619047, totalHourlyRate, 0.01);
    }

    @Test
    public void testCalculateEmployeeHourlyRate() {
        // Create a sample employee
        Employees employee = new Employees(5, "Tove Lo", 33000, 30, 1800, 1500, 1200, "Denmark");

        double hourlyRate = mainViewController.calculateEmployeeHourlyRate(employee);

        assertEquals(30.96, hourlyRate, 0.01);
    }

      }