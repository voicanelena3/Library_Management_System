package library.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import library.model.Borrower;
import library.model.Loan;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class BorrowerXMLHandler {

    /**
     * Helper class for JAXB to unmarshal the root list of borrowers.
     */
    @XmlRootElement(name = "borrowers")
    private static class BorrowersWrapper {
        private List<Borrower> borrower;
        public List<Borrower> getBorrower() { return borrower; }
        public void setBorrower(List<Borrower> borrower) { this.borrower = borrower; }
    }

    /**
     * Loads a list of Borrower objects from an XML file using a File path.
     */
    public static List<Borrower> loadAll(String filename) {
        try {
            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(BorrowersWrapper.class, Borrower.class, Loan.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BorrowersWrapper wrapper = (BorrowersWrapper) jaxbUnmarshaller.unmarshal(file);
            return wrapper.getBorrower();
        } catch (JAXBException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Loads a list of Borrower objects from an XML resource stream (more robust).
     * @param inputStream The InputStream to read the XML from.
     * @return A list of borrowers, or an empty list if an error occurs.
     */
    public static List<Borrower> loadAll(InputStream inputStream) {
        if (inputStream == null) {
            System.err.println("Input stream for borrowers.xml is null. Resource not found.");
            return Collections.emptyList();
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BorrowersWrapper.class, Borrower.class, Loan.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BorrowersWrapper wrapper = (BorrowersWrapper) jaxbUnmarshaller.unmarshal(inputStream);
            return wrapper.getBorrower();
        } catch (JAXBException e) {
            e.printStackTrace();
            System.err.println("JAXB Exception occurred while unmarshalling the XML stream. Check for syntax errors.");
            return Collections.emptyList();
        }
    }
}