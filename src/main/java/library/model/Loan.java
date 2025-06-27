package library.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import library.util.LocalDateAdapter; // Import custom LocalDateAdapter

import java.io.Serializable;
import java.time.LocalDate; // Use java.time.LocalDate
import java.time.temporal.ChronoUnit; // Import ChronoUnit for date difference

/**
 * Represents a single borrowing transaction (a loan).
 * This class links a book to a borrower with issue, due, and return dates.
 */
@XmlRootElement(name = "loan")
@XmlAccessorType(XmlAccessType.FIELD) // Crucial for field-based mapping
public class Loan implements Serializable {

    @XmlElement // For serialization by JAXB
    private int recordId; // Unique ID for each loan record

    @XmlElement(name = "loanedBook")
    private Book loanedBook; // The book that is loaned

    @XmlElement(name = "issuedDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class) // Use the new LocalDateAdapter
    private LocalDate issuedDate; // Date when the book was issued

    @XmlElement(name = "dueDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class) // Use the new LocalDateAdapter
    private LocalDate dueDate; // Date when the book is due

    @XmlElement(name = "returnedDate", nillable = true) // Allow this field to be null in XML
    @XmlJavaTypeAdapter(LocalDateAdapter.class) // Use the new LocalDateAdapter
    private LocalDate returnedDate; // Date when the book was actually returned (can be null if not returned)

    @XmlElement // For serialization by JAXB
    private boolean finePaid; // Tracks if any incurred fine has been paid

    // No-argument constructor required by JAXB
    public Loan() {
        // Default constructor sets finePaid to false by default
        this.finePaid = false;
    }

    /**
     * Constructor for creating a new loan when a book is issued.
     * The due date is calculated based on the issued date and a hardcoded loan period.
     */
    public Loan(int recordId, Book loanedBook, LocalDate issuedDate) {
        this.recordId = recordId;
        this.loanedBook = loanedBook;
        this.issuedDate = issuedDate;

        // Calculate dueDate based on a hardcoded loan period (e.g., 14 days)
        int loanPeriodDays = 14; // Hardcoded loan period in days
        this.dueDate = issuedDate.plusDays(loanPeriodDays);

        this.returnedDate = null; // Initially not returned
        this.finePaid = false;
    }

    /**
     * Full constructor for JAXB to unmarshal (read from XML).
     * This constructor would typically be used internally by JAXB.
     */
    public Loan(int recordId, Book loanedBook, LocalDate issuedDate, LocalDate dueDate, LocalDate returnedDate, boolean finePaid) {
        this.recordId = recordId;
        this.loanedBook = loanedBook;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
        this.finePaid = finePaid;
    }

    // --- Getters ---
    public int getRecordId() { return recordId; }
    public Book getLoanedBook() { return loanedBook; }
    public LocalDate getIssuedDate() { return issuedDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnedDate() { return returnedDate; }
    public boolean isFinePaid() { return finePaid; }

    // --- Setters ---
    public void setRecordId(int recordId) { this.recordId = recordId; }
    public void setLoanedBook(Book loanedBook) { this.loanedBook = loanedBook; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setReturnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; }
    public void setFinePaid(boolean finePaid) { this.finePaid = finePaid; }

    /**
     * Computes the fine based on the return date (if returned) or current date (if not returned yet).
     * Compares against the due date.
     *
     * @return The fine amount. Returns 0.0 if not overdue or if the fine is already paid.
     */
    public double computeFine() {
        if (finePaid) {
            return 0.0; // No fine if already paid
        }

        LocalDate effectiveReturnDate = (returnedDate != null) ? returnedDate : LocalDate.now();

        // If returned before or on due date, or not yet returned and current date is before due date
        if (effectiveReturnDate.isBefore(dueDate) || effectiveReturnDate.isEqual(dueDate)) {
            return 0.0;
        }

        // If overdue, calculate days overdue and apply hardcoded fine rate
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, effectiveReturnDate);

        // Hardcoded fine per day
        double finePerDay = 0.5; // Example: 0.5 units per day

        return daysOverdue * finePerDay;
    }

    /**
     * Marks the fine as paid.
     */
    public void payFine() {
        this.finePaid = true;
    }

    /**
     * Checks if the loan is currently active (not returned yet).
     * @return true if the book has not been returned, false otherwise.
     */
    public boolean isActive() {
        return returnedDate == null;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "recordId=" + recordId +
                ", loanedBook=" + (loanedBook != null ? loanedBook.getTitle() : "N/A") +
                ", issuedDate=" + issuedDate +
                ", dueDate=" + dueDate +
                ", returnedDate=" + returnedDate +
                ", finePaid=" + finePaid +
                '}';
    }
}