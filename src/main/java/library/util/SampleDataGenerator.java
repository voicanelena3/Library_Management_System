package library.util;

import library.model.Book;
import library.model.Borrower;
import library.model.Loan; // We only use the Loan class now.
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates clean, consistent sample data for the library application.
 * This generator aligns with the unified data model, using only Loan objects
 * for both active and historical borrowing records. It does NOT use the
 * deprecated BorrowingRecord class.
 */
public class SampleDataGenerator {

    public static void main(String[] args) {
        try {
            // Define file paths
            String booksFilePath = "src/main/resources/data/books.xml";
            String borrowersFilePath = "src/main/resources/data/borrowers.xml";

            // Create the data directory if it doesn't exist
            File dataDir = new File("src/main/resources/data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            // --- 1. Generate Sample Books ---
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


            // --- 2. Generate Sample Borrowers ---
            List<Borrower> borrowers = new ArrayList<>();
            Borrower alice = new Borrower(1, "Alice", "password123", "123 Main St", 1234567890, "alice@example.com");
            Borrower bob = new Borrower(2, "Bob", "secret456", "456 Elm St", 987654321, "bob@example.com");
            Borrower librarian = new Borrower(9999, "Librarian", "librarianpass", "Library HQ", 1122334455, "librarian@example.com", "Librarian");


            // --- 3. Generate ACTIVE and HISTORICAL Loans and add them to Borrowers ---

            // Loan 1 for Alice (Book 101) - ACTIVE
            book1.setBorrowed(true); // Mark the physical book as borrowed
            Loan loan1Alice = new Loan(1, book1, LocalDate.now().minusDays(10));
            alice.addLoan(loan1Alice);

            // Loan 2 for Alice (Book 103) - ACTIVE (and overdue)
            book3.setBorrowed(true);
            Loan loan2Alice = new Loan(2, book3, LocalDate.now().minusDays(20)); // Due date was 6 days ago
            alice.addLoan(loan2Alice);

            // Loan 3 for Bob (Book 102) - HISTORICAL (returned in the past)
            // Note: The book is NOT marked as borrowed because it has been returned.
            book2.setBorrowed(false);
            Loan loan1Bob = new Loan(3, book2, LocalDate.now().minusDays(30)); // Issued 30 days ago
            loan1Bob.setReturnedDate(LocalDate.now().minusDays(15)); // Returned 15 days ago
            bob.addLoan(loan1Bob);

            // Loan 4 for Bob (Book 106) - ACTIVE
            book6.setBorrowed(true);
            Loan loan2Bob = new Loan(4, book6, LocalDate.now().minusDays(3));
            bob.addLoan(loan2Bob);


            // Add the borrowers to the main list
            borrowers.add(alice);
            borrowers.add(bob);
            borrowers.add(librarian);


            // --- 4. Save all data using the correct XML handlers ---
            // This saves the books with their current 'isBorrowed' status
            BookXMLHandler.saveAll(books, booksFilePath);
            System.out.println("Sample books saved to " + booksFilePath);

            // This saves the borrowers, which now includes their full loan histories (active and returned).
            BorrowerXMLHandler.saveAll(borrowers, borrowersFilePath);
            System.out.println("Sample borrowers with their loans saved to " + borrowersFilePath);

            System.out.println("\nSample data generation complete. The application is ready to run.");

        } catch (Exception e) {
            System.err.println("An error occurred during sample data generation.");
            e.printStackTrace();
        }
    }
}
