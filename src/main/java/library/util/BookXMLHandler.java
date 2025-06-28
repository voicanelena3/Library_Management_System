package library.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import library.model.Book;
import library.model.BookList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookXMLHandler {

    public static List<Book> loadAll(String filePath) {
        try {
            File xmlFile = new File(filePath);

            if (!xmlFile.exists() || xmlFile.length() == 0) {
                System.out.println("INFO: Books file not found or is empty. Starting with an empty list.");
                return new ArrayList<>();
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(BookList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BookList bookList = (BookList) jaxbUnmarshaller.unmarshal(xmlFile);
            return bookList.getBooks();

        } catch (Exception e) {
            System.err.println("Error loading books from XML: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static void saveAll(List<Book> books, String filePath) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BookList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            BookList bookList = new BookList();
            bookList.setBooks(books);

            jaxbMarshaller.marshal(bookList, new File(filePath));

        } catch (Exception e) {
            System.err.println("Error saving books to XML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}