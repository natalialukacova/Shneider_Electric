package be;

public class Employees {
    private int id;
    private String employeeName;
    private double salary;
    private double multiplier;
    private double configurableAmount;
    private String team;
    private double workingHours;
    private double utilizationPercentage;
    private double overheadCost;
    private double hourlyRate;
    private String geography;


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

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public double getConfigurableAmount() {
        return configurableAmount;
    }

    public void setConfigurableAmount(Double configurableAmount) {
        this.configurableAmount = configurableAmount;
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

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }

    public double getUtilizationPercentage() {
        return utilizationPercentage;
    }

    public void setUtilizationPercentage(Double utilizationPercentage) {
        this.utilizationPercentage = utilizationPercentage;
    }

    public double getOverheadCost() {
        return overheadCost;
    }

    public void setOverheadCost(Double overheadCost) {
        this.overheadCost = overheadCost;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getGeography() {
        return geography;
    }

    public void setGeography(String geography) {
        this.geography = geography;
    }

    public Employees(int id, String employeeName, double salary, double multiplier, double configurableAmount, double workingHours, double overheadCost, String geography) {
        this.id = id;
        this.employeeName = employeeName;
        this.salary = salary;
        this.multiplier = multiplier;
        this.configurableAmount = configurableAmount;
        this.workingHours = workingHours;
        this.overheadCost = overheadCost;
        this.geography = geography;

    }


    @Override
    public String toString(){
        return id + "" + salary + "" + multiplier + "" + configurableAmount + "" + workingHours + "" + overheadCost + "" + hourlyRate + "" + geography;
    }
}
