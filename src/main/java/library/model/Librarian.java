package library.model;

import java.io.Serializable;
import java.util.Calendar;

public class Librarian extends Staff implements Serializable {
    private int officeNumber;
    private String department;
    private Calendar workSchedule;
    private int yearsOfExperience;

    public Librarian() {
        super();
    }

    public Librarian(int id, String name, String password, String address, int phone, String email,
                     double salary, String type, int officeNumber, String department,
                     Calendar workSchedule, int yearsOfExperience) {
        super(id, name, password, address, phone, email, salary, type);
        this.officeNumber = officeNumber;
        this.department = department;
        this.workSchedule = workSchedule;
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(int officeNumber) { this.officeNumber = officeNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Calendar getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(Calendar workSchedule) { this.workSchedule = workSchedule; }

    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    @Override
    public String toString() {
        return super.toString() + ", Librarian{" +
                "officeNumber=" + officeNumber +
                ", department='" + department + '\'' +
                ", workSchedule=" + (workSchedule != null ? workSchedule.getTime() : "N/A") +
                ", yearsOfExperience=" + yearsOfExperience +
                '}';
    }
}
