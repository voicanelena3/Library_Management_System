package library.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "borrowers")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowerList {

    @XmlElement(name = "borrower")
    private List<Borrower> borrowers;

    public BorrowerList() {
        this.borrowers = new ArrayList<>();
    }

    public BorrowerList(List<Borrower> borrowers) {
        this.borrowers = borrowers;
    }

    public List<Borrower> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(List<Borrower> borrowers) {
        this.borrowers = borrowers;
    }
}