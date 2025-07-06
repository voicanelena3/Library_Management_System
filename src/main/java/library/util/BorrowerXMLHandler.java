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

    public static void saveAll(List<Borrower> borrowers, String filename) throws JAXBException {
        BorrowersWrapper wrapper = new BorrowersWrapper();
        wrapper.setBorrowers(borrowers);

        JAXBContext jaxbContext = JAXBContext.newInstance(BorrowersWrapper.class, Borrower.class, Loan.class, Book.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        jaxbMarshaller.marshal(wrapper, new File(filename));
    }
}