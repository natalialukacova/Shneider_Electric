package be;

public class Employees {
    private int id;
    private String employeeName;
    private double salary;
    private double multiplier;
    private double configurableAmount;
    private String country;
    private String team;
    private double workingHours;
    private double utilizationPercentage;
    private double overheadCost;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public double getConfigurableAmount() {
        return configurableAmount;
    }

    public void setConfigurableAmount(int configurableAmount) {
        this.configurableAmount = configurableAmount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public double getUtilizationPercentage() {
        return utilizationPercentage;
    }

    public void setUtilizationPercentage(int utilizationPercentage) {
        this.utilizationPercentage = utilizationPercentage;
    }

    public double getOverheadCost() {
        return overheadCost;
    }

    public void setOverheadCost(int overheadCost) {
        this.overheadCost = overheadCost;
    }

    public Employees(int id, String employeeName, double salary, double multiplier, double configurableAmount, String country, String team, double workingHours, double utilizationPercentage, double overheadCost) {
        this.id = id;
        this.employeeName = employeeName;
        this.salary = salary;
        this.multiplier = multiplier;
        this.configurableAmount = configurableAmount;
        this.country = country;
        this.team = team;
        this.workingHours = workingHours;
        this.utilizationPercentage = utilizationPercentage;
        this.overheadCost = overheadCost;
    }

    @Override
    public String toString(){
        return id + "" + salary + "" + multiplier + "" + configurableAmount + "" + country + "" + team + "" + workingHours + "" + utilizationPercentage + "" + overheadCost;
    }
}
