
package library.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import library.model.BorrowingRecord;
import library.model.BorrowingRecordList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRecordXMLHandler {

    public static List<BorrowingRecord> loadAll(String filePath) {
        try {
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


    public static void saveAll(List<BorrowingRecord> records, String filePath) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BorrowingRecordList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            BorrowingRecordList borrowingRecordList = new BorrowingRecordList();
            borrowingRecordList.setRecords(records);

            jaxbMarshaller.marshal(borrowingRecordList, new File(filePath));
            System.out.println("Borrowing records data saved successfully to " + filePath);

        } catch (Exception e) {
            System.err.println("Error saving borrowing records to XML at " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}