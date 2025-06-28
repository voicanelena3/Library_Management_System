package library.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.io.Serializable;


@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Person implements Serializable {

    @XmlElement // JAXB va mapa <id> la acest câmp
    protected int id;
    @XmlElement // JAXB va mapa <name> la acest câmp
    protected String name;
    @XmlElement // JAXB va mapa <password> la acest câmp
    protected String password;
    @XmlElement // JAXB va mapa <address> la acest câmp
    protected String address;
    @XmlElement // JAXB va mapa <phoneNumber> la acest câmp
    protected int phoneNumber;
    @XmlElement // JAXB va mapa <email> la acest câmp
    protected String email;
    @XmlElement // JAXB va mapa <role> la acest câmp
    protected String role;

    // Constructor no-arg necesar pentru JAXB
    public Person() {
    }

    // Constructor pentru Login (ID, Nume, Parola, Rol) - fără adresa/telefon/email inițial
    public Person(int id, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Constructor complet pentru a crea Person cu toate detaliile
    public Person(int id, String name, String password, String address, int phoneNumber, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }

    // Getters for all fields
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public int getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    // Setters for all fields
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + (address != null ? address : "N/A") + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", email='" + (email != null ? email : "N/A") + '\'' +
                ", role='" + (role != null ? role : "N/A") + '\'' +
                '}';
    }
}