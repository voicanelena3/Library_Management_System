package library.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import library.util.LocalDateAdapter;
import java.time.LocalDate;
import java.util.Random;

@XmlRootElement(name = "borrowingRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowingRecord {

    private int recordId;
    private int borrowerId;
    private int bookId;
    private int quantity;
    private LocalDate issuedDate;
    private LocalDate dueDate;


    public BorrowingRecord(int recordId, int borrowerId, int bookId, LocalDate issuedDate, LocalDate dueDate) {
        this.recordId = recordId;
        this.borrowerId = borrowerId;
        this.bookId = bookId;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
    }

    public BorrowingRecord() {
        // Required for JAXB
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    // ... other getters and setters for bookId, borrowerId, etc.
}