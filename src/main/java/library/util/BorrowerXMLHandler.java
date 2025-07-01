package library.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller; // Import Marshaller for saving
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import library.model.Book; // Required for JAXB context
import library.model.Borrower;
import library.model.Loan;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class BorrowerXMLHandler {

    /**
     * Helper class for JAXB to wrap the list, creating a <borrowers> root element.
     */
    @XmlRootElement(name = "borrowers")
    private static class BorrowersWrapper {
        private List<Borrower> borrowers;
        @XmlElement(name = "borrower")
        public List<Borrower> getBorrowers() {
            return borrowers;
        }

        public void setBorrowers(List<Borrower> borrowers) {
            this.borrowers = borrowers;
        }
    }

    /**
     * Loads a list of Borrower objects from an XML file path.
     * @param filename The path to the XML file.
     * @return A list of borrowers, or an empty list on error.
     */
    public static List<Borrower> loadAll(String filename) {
        try {
            File file = new File(filename);

            JAXBContext jaxbContext = JAXBContext.newInstance(BorrowersWrapper.class, Borrower.class, Loan.class, Book.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BorrowersWrapper wrapper = (BorrowersWrapper) jaxbUnmarshaller.unmarshal(file);
            return wrapper.getBorrowers();
        } catch (JAXBException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Loads a list of Borrower objects from an XML resource stream.
     * @param inputStream The InputStream to read the XML from.
     * @return A list of borrowers, or an empty list on error.
     */
    public static List<Borrower> loadAll(InputStream inputStream) {
        if (inputStream == null) {
            System.err.println("Input stream for borrowers.xml is null. Resource not found.");
            return Collections.emptyList();
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BorrowersWrapper.class, Borrower.class, Loan.class, Book.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BorrowersWrapper wrapper = (BorrowersWrapper) jaxbUnmarshaller.unmarshal(inputStream);
            return wrapper.getBorrowers();
        } catch (JAXBException e) {
            e.printStackTrace();
            System.err.println("JAXB Exception occurred while unmarshalling the XML stream. Check for syntax errors.");
            return Collections.emptyList();
        }
    }

    /**
     * NEW METHOD: Saves a list of Borrower objects to a specified XML file.
     *
     * @param borrowers The list of borrowers to save.
     * @param filename  The path of the file to save to.
     * @throws JAXBException if an error occurs during the XML saving process.
     */
    public static void saveAll(List<Borrower> borrowers, String filename) throws JAXBException {
        BorrowersWrapper wrapper = new BorrowersWrapper();
        wrapper.setBorrowers(borrowers);

        JAXBContext jaxbContext = JAXBContext.newInstance(BorrowersWrapper.class, Borrower.class, Loan.class, Book.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        jaxbMarshaller.marshal(wrapper, new File(filename));
    }
}