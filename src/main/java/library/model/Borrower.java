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

    @XmlElementWrapper(name = "borrowedLoans")
    @XmlElement(name = "loan")
    private List<Loan> borrowedLoans = new ArrayList<>();

    // Câmpul 'role' este moștenit de la Person, nu mai trebuie declarat aici.

    public Borrower() {
        super(); // Va apela Person()
        this.borrowedLoans = new ArrayList<>();
    }


    public Borrower(int id, String name, String password, String address, int phoneNumber, String email) {
        super(id, name, password, address, phoneNumber, email, "Borrower");
        this.borrowedLoans = new ArrayList<>();
    }


    public Borrower(int id, String name, String password, String address, int phoneNumber, String email, String role) {
        super(id, name, password, address, phoneNumber, email, role);
        this.borrowedLoans = new ArrayList<>();
    }


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


    public List<Loan> getBorrowedLoans() {
        if (borrowedLoans == null) {
            borrowedLoans = new ArrayList<>();
        }
        return borrowedLoans;
    }

    public void setBorrowedLoans(List<Loan> loans) {
        this.borrowedLoans = loans;
    }

    public List<Book> getBorrowedBooks() {
        if (borrowedLoans == null) {
            return new ArrayList<>();
        }
        return borrowedLoans.stream()
                .map(Loan::getLoanedBook)
                .collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return super.toString() + ", BorrowedLoans=" + getBorrowedLoans().size();
    }
}