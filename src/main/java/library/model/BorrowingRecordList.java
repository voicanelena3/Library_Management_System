// In file: src/main/java/library/model/BorrowingRecordList.java

package library.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for a list of BorrowingRecord objects for JAXB.
 * This class is needed to create a root element in the XML file.
 */
@XmlRootElement(name = "borrowingRecords")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowingRecordList {

    @XmlElement(name = "record")
    private List<BorrowingRecord> records = new ArrayList<>();

    // No-argument constructor required for JAXB
    public BorrowingRecordList() {
    }

    // --- Getters and Setters ---
    public List<BorrowingRecord> getRecords() {
        return records;
    }

    public void setRecords(List<BorrowingRecord> records) {
        this.records = records;
    }
}