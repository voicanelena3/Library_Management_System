// In file: src/main/java/library/util/BorrowingRecordXMLHandler.java

package library.util; // CORECTAT: Pachetul trebuie să fie 'library.util'

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import library.model.BorrowingRecord; // ADAUGAT: Trebuie sa importati clasa BorrowingRecord
import library.model.BorrowingRecordList; // Importul este deja corect
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRecordXMLHandler {

    /**
     * Loads all borrowing records from an XML file.
     *
     * @param filePath The path to the XML file.
     * @return A list of BorrowingRecord objects.
     */
    public static List<BorrowingRecord> loadAll(String filePath) {
        try {
            // Get the URL for the resource file
            File xmlFile = new File(filePath);

            if (!xmlFile.exists() || xmlFile.length() == 0) {
                System.out.println("INFO: Borrowing records file not found or is empty at " + xmlFile.getAbsolutePath() + ". Starting with an empty list.");
                return new ArrayList<>();
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(BorrowingRecordList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BorrowingRecordList borrowingRecordList = (BorrowingRecordList) jaxbUnmarshaller.unmarshal(xmlFile);
            return borrowingRecordList.getRecords();

        } catch (Exception e) {
            System.err.println("Error loading borrowing records from XML at " + filePath + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list on error
        }
    }

    /**
     * Saves a list of borrowing records to an XML file.
     *
     * @param records The list of records to save.
     * @param filePath The path to the XML file.
     */
    public static void saveAll(List<BorrowingRecord> records, String filePath) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BorrowingRecordList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // Set a property to format the XML output nicely
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Create a list wrapper and set the records
            BorrowingRecordList borrowingRecordList = new BorrowingRecordList();
            borrowingRecordList.setRecords(records);

            // Marshal the object to the file
            jaxbMarshaller.marshal(borrowingRecordList, new File(filePath));
            System.out.println("Borrowing records data saved successfully to " + filePath);

        } catch (Exception e) {
            System.err.println("Error saving borrowing records to XML at " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ELIMINAT: Metoda duplicat și goală a fost eliminată
}