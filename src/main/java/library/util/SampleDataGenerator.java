package library.util;

import library.model.Book;
import library.model.Borrower;
import library.model.BorrowingRecord;
import library.model.Loan; // IMPORTANT: We now use the Loan class for active loans
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleDataGenerator {
    public static void main(String[] args) throws Exception {
        // Asigurați-vă că folderul src/main/resources/data/ există!
        String booksFilePath = "src/main/resources/data/books.xml";
        String borrowersFilePath = "src/main/resources/data/borrowers.xml";
        String borrowingRecordsFilePath = "src/main/resources/data/borrowing_records.xml";

        // Create the data directory if it doesn't exist
        File dataDir = new File("src/main/resources/data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // --- Generate Sample Books (and mark some as borrowed) ---
        List<Book> books = new ArrayList<>();
        Book book1 = new Book(101, "The Lord of the Rings", "J.R.R. Tolkien", "Fantasy");
        Book book2 = new Book(102, "Pride and Prejudice", "Jane Austen", "Romance");
        Book book3 = new Book(103, "1984", "George Orwell", "Dystopian");
        Book book4 = new Book(104, "To Kill a Mockingbird", "Harper Lee", "Fiction");
        Book book5 = new Book(105, "The Great Gatsby", "F. Scott Fitzgerald", "Classic");
        Book book6 = new Book(106, "Dune", "Frank Herbert", "Science Fiction");

        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
        books.add(book6);

        // --- Generate Sample Borrowers and ADD ACTIVE LOANS to them ---
        List<Borrower> borrowers = new ArrayList<>();

        // Create the borrowers
        Borrower alice = new Borrower(1, "Alice", "password123", "123 Main St", 1234567890, "alice@example.com");
        Borrower bob = new Borrower(2, "Bob", "secret456", "456 Elm St", 987654321, "bob@example.com");
        Borrower librarian = new Borrower(9999, "Librarian", "librarianpass", "Library HQ", 1122334455, "librarian@example.com", "Librarian");

        // --- Step 1: Create active Loan objects and add them to the borrowers ---
        // These are the records that will show up in the Borrower's UI

        // Loan 1 for Alice (book 101) - Active
        book1.setBorrowed(true); // Mark the book as borrowed
        // Loan constructor: recordId, book, issuedDate
        Loan loan1Alice = new Loan(1, book1, LocalDate.now().minusDays(10));
        alice.addLoan(loan1Alice);

        // Loan 2 for Alice (book 103) - Active
        book3.setBorrowed(true); // Mark the book as borrowed
        Loan loan2Alice = new Loan(2, book3, LocalDate.now().minusDays(2));
        alice.addLoan(loan2Alice);

        // Loan 3 for Bob (book 102) - Active
        book2.setBorrowed(true);
        Loan loan1Bob = new Loan(3, book2, LocalDate.now().minusDays(5));
        bob.addLoan(loan1Bob);

        // Add users to the list
        borrowers.add(alice);
        borrowers.add(bob);
        borrowers.add(librarian);

        // --- Step 2: Save all data using the XML handlers ---
        // This will save the books with their 'borrowed' status
        BookXMLHandler.saveAll(books, booksFilePath);
        System.out.println("Sample books saved to " + booksFilePath);

        // This will save the borrowers with their ACTIVE LOANS!
        BorrowerXMLHandler.saveAll(borrowers, borrowersFilePath);
        System.out.println("Sample borrowers with active loans saved to " + borrowersFilePath);

        // --- Generate HISTORICAL Borrowing Records (for the calendar view) ---
        // This is a separate list for tracking historical events (returned books, etc.)
        List<BorrowingRecord> historicalRecords = new ArrayList<>();
        // Alice (ID 1) returned book 104 in the past
        historicalRecords.add(new BorrowingRecord(
                4, 1, 104, LocalDate.now().minusDays(30), LocalDate.now().minusDays(16), LocalDate.now().minusDays(16), false));
        // Bob (ID 2) returned book 105 in the past
        historicalRecords.add(new BorrowingRecord(
                5, 2, 105, LocalDate.now().minusDays(20), LocalDate.now().minusDays(5), LocalDate.now().minusDays(5), true));

        BorrowingRecordXMLHandler.saveAll(historicalRecords, borrowingRecordsFilePath);
        System.out.println("Sample historical borrowing records saved to " + borrowingRecordsFilePath);
    }
}