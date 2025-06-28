package library.util;

import library.model.Book;
import library.model.Borrower;
import library.model.Loan; // We only use the Loan class now.
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleDataGenerator {

    public static void main(String[] args) {
        try {
            String booksFilePath = "src/main/resources/data/books.xml";
            String borrowersFilePath = "src/main/resources/data/borrowers.xml";
 File dataDir = new File("src/main/resources/data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

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

            List<Borrower> borrowers = new ArrayList<>();
            Borrower alice = new Borrower(1, "Alice", "password123", "123 Main St", 1234567890, "alice@example.com");
            Borrower bob = new Borrower(2, "Bob", "secret456", "456 Elm St", 987654321, "bob@example.com");
            Borrower librarian = new Borrower(9999, "Librarian", "librarianpass", "Library HQ", 1122334455, "librarian@example.com", "Librarian");




            book1.setBorrowed(true);
            Loan loan1Alice = new Loan(1, book1, LocalDate.now().minusDays(10));
            alice.addLoan(loan1Alice);
            book3.setBorrowed(true);
            Loan loan2Alice = new Loan(2, book3, LocalDate.now().minusDays(20)); // Due date was 6 days ago
            alice.addLoan(loan2Alice);
            book2.setBorrowed(false);
            Loan loan1Bob = new Loan(3, book2, LocalDate.now().minusDays(30)); // Issued 30 days ago
            loan1Bob.setReturnedDate(LocalDate.now().minusDays(15)); // Returned 15 days ago
            bob.addLoan(loan1Bob);
            book6.setBorrowed(true);
            Loan loan2Bob = new Loan(4, book6, LocalDate.now().minusDays(3));
            bob.addLoan(loan2Bob);

            borrowers.add(alice);
            borrowers.add(bob);
            borrowers.add(librarian);

            BookXMLHandler.saveAll(books, booksFilePath);
            System.out.println("Sample books saved to " + booksFilePath);

            BorrowerXMLHandler.saveAll(borrowers, borrowersFilePath);
            System.out.println("Sample borrowers with their loans saved to " + borrowersFilePath);

            System.out.println("\nSample data generation complete. The application is ready to run.");

        } catch (Exception e) {
            System.err.println("An error occurred during sample data generation.");
            e.printStackTrace();
        }
    }
}
