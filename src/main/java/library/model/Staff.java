package library.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "staff")
@XmlAccessorType(XmlAccessType.FIELD)
public class Staff extends Person implements Serializable {

    @XmlElement
    private double salary;
    @XmlElement
    private String type; // Intern, Assistant Librarian, etc.

    // No-argument constructor (REQUIRED BY JAXB)
    public Staff() {
        super();
    }

    // --- Constructor actualizat pentru compatibilitate (pentru a apela super()) ---
    // Atentie: Aici trimitem "Librarian" ca rol implicit catre clasa parinte.
    public Staff(int id, String name, String password, String address, int phone, String email,
                 double salary, String type) {
        // Apelam constructorul complet din Person, inclusiv rolul.
        // Asigurati-va ca Person are un constructor cu 7 argumente.
        super(id, name, password, address, phone, email, "Librarian");
        this.salary = salary;
        this.type = type;
    }

    // --- Constructor complet (optional, dar recomandat) ---
    // Acesta permite setarea rolului in mod explicit.
    public Staff(int id, String name, String password, String address, int phone, String email, String role,
                 double salary, String type) {
        super(id, name, password, address, phone, email, role);
        this.salary = salary;
        this.type = type;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() + ", Staff{" +
                "salary=" + salary +
                ", type='" + type + '\'' +
                '}';
    }
}