package library.model;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@XmlRootElement(name = "borrower")
// Nu mai e nevoie de @XmlAccessorType(XmlAccessType.FIELD) aici, este moștenită de la Person
public class Borrower extends Person implements Serializable {

    @XmlElementWrapper(name = "borrowedLoans") // Wrapper element for the list <borrowedLoans>
    @XmlElement(name = "loan") // Element for each item in the list <loan>
    private List<Loan> borrowedLoans = new ArrayList<>();

    // Câmpul 'role' este moștenit de la Person, nu mai trebuie declarat aici.

    // No-argument constructor (REQUIRED BY JAXB)
    public Borrower() {
        super(); // Va apela Person()
        this.borrowedLoans = new ArrayList<>();
    }

    // Constructor cu 6 argumente, pentru adăugare borrower standard (implicit "Borrower" role)
    public Borrower(int id, String name, String password, String address, int phoneNumber, String email) {
        // Apelează constructorul complet din Person cu rolul "Borrower"
        super(id, name, password, address, phoneNumber, email, "Borrower");
        this.borrowedLoans = new ArrayList<>();
    }

    // Constructor cu 7 argumente, pentru încărcare din XML sau când rolul este specificat explicit
    public Borrower(int id, String name, String password, String address, int phoneNumber, String email, String role) {
        // Apelează constructorul complet din Person
        super(id, name, password, address, phoneNumber, email, role);
        this.borrowedLoans = new ArrayList<>();
    }

    // --- Loan Management Methods ---
    public void addLoan(Loan loan) {
        if (borrowedLoans == null) {
            borrowedLoans = new ArrayList<>();
        }
        borrowedLoans.add(loan);
    }

    public void removeLoan(Loan loan) {
        if (borrowedLoans != null) {
            borrowedLoans.remove(loan);
        }
    }

    public void removeLoanByBookId(int bookId) {
        if (borrowedLoans != null) {
            borrowedLoans.removeIf(loan -> Objects.equals(loan.getLoanedBook().getBookID(), bookId));
        }
    }

    // Getters/Setters for borrowedLoans
    public List<Loan> getBorrowedLoans() {
        if (borrowedLoans == null) {
            borrowedLoans = new ArrayList<>();
        }
        return borrowedLoans;
    }

    public void setBorrowedLoans(List<Loan> loans) {
        this.borrowedLoans = loans;
    }

    // Metoda convenabilă pentru a obține lista de cărți din Loan-uri (pentru compatibilitate)
    public List<Book> getBorrowedBooks() {
        if (borrowedLoans == null) {
            return new ArrayList<>();
        }
        return borrowedLoans.stream()
                .map(Loan::getLoanedBook)
                .collect(Collectors.toList());
    }

    // Metodele getRole() și setRole() sunt moștenite de la Person.
    // Nu mai este nevoie să le declarați aici.

    @Override
    public String toString() {
        // Va apela toString din Person care include rolul
        return super.toString() + ", BorrowedLoans=" + getBorrowedLoans().size();
    }
}