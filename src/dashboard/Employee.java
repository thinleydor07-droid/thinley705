package dashboard;

public class Employee {
    public String id;
    public String name;
    public String dept;
    public String status; // Present, Absent, On Leave
    public boolean salaryProcessed;

    public Employee(String id, String name, String dept, String status, boolean salaryProcessed) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.status = status;
        this.salaryProcessed = salaryProcessed;
    }
}
