package library.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;


public class LoanTest {

    private Book testBook;

    // Adnotarea @Before face ca această metodă să ruleze înaintea fiecărui test.
    // Este utilă pentru a pregăti obiecte comune.
    @Before
    public void setUp() {
        testBook = new Book(999, "Test Book", "Test Author", "Testing");
    }

    /**
     * Testează scenariul în care o carte nu este returnată cu întârziere.
     * Rezultatul așteptat: amenda este 0.
     */
    @Test
    public void testComputeFine_NoFineWhenNotOverdue() {
        // Arrange: Creează un împrumut care nu este întârziat.
        LocalDate issueDate = LocalDate.now().minusDays(5);
        Loan loan = new Loan(1, testBook, issueDate); // Due date este în 9 zile
        loan.setReturnedDate(LocalDate.now()); // Returnată astăzi

        // Act: Calculează amenda.
        double fine = loan.computeFine();

        // Assert: Verifică dacă amenda este 0.
        // Al treilea parametru (delta) este pentru a gestiona erorile de precizie la numerele cu virgulă.
        assertEquals(0.0, fine, 0.001);
    }

    /**
     * Testează scenariul în care o carte este returnată cu 10 zile întârziere.
     * Rezultatul așteptat: amenda este 5.0 (10 zile * 0.5/zi).
     */
    @Test
    public void testComputeFine_CorrectFineWhenOverdue() {
        // Arrange: Creează un împrumut cu 24 de zile în urmă (scadent acum 10 zile).
        LocalDate issueDate = LocalDate.now().minusDays(24);
        Loan loan = new Loan(2, testBook, issueDate); // Due date era acum 10 zile
        loan.setReturnedDate(LocalDate.now()); // Returnată astăzi

        // Act: Calculează amenda.
        double fine = loan.computeFine();

        // Assert: Verifică dacă amenda este 5.0.
        assertEquals(5.0, fine, 0.001);
    }

    /**
     * Testează scenariul în care o carte este întârziată, dar amenda a fost deja plătită.
     * Rezultatul așteptat: amenda este 0.
     */
    @Test
    public void testComputeFine_NoFineWhenAlreadyPaid() {
        // Arrange: Creează un împrumut întârziat.
        LocalDate issueDate = LocalDate.now().minusDays(30);
        Loan loan = new Loan(3, testBook, issueDate);
        loan.setReturnedDate(LocalDate.now());

        // Acționează asupra stării inițiale: marchează amenda ca fiind plătită.
        loan.payFine();

        // Act: Calculează amenda din nou.
        double fine = loan.computeFine();

        // Assert: Verifică dacă amenda este 0, chiar dacă împrumutul a fost întârziat.
        assertEquals(0.0, fine, 0.001);
    }

    /**
     * Testează dacă metoda isActive() returnează corect 'true' pentru un împrumut activ.
     */
    @Test
    public void testIsActive_ReturnsTrueForActiveLoan() {
        // Arrange: Creează un împrumut fără dată de returnare.
        Loan loan = new Loan(4, testBook, LocalDate.now());

        // Act & Assert: Verifică direct dacă metoda returnează true.
        assertTrue("Loan should be active when return date is null", loan.isActive());
    }

    /**
     * Testează dacă metoda isActive() returnează corect 'false' pentru un împrumut încheiat.
     */
    @Test
    public void testIsActive_ReturnsFalseForReturnedLoan() {
        // Arrange: Creează un împrumut cu dată de returnare.
        Loan loan = new Loan(5, testBook, LocalDate.now().minusDays(10));
        loan.setReturnedDate(LocalDate.now());

        // Act & Assert: Verifică direct dacă metoda returnează false.
        assertFalse("Loan should be inactive when return date is set", loan.isActive());
    }
}