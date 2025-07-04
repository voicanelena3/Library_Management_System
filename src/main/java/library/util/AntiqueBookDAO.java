package library.util;

import library.model.AntiqueBook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.text.html.parser.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntiqueBookDAO {
    // Obține toate cărțile de la anticariat
    public List<AntiqueBook> getAllAntiqueBooks() {
        List<AntiqueBook> books = new ArrayList<>();
        String sql = "SELECT * FROM antique_books";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AntiqueBook book = new AntiqueBook();
                book.setId(rs.getInt("antique_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublishedYear(rs.getInt("published_year"));
                book.setPrice(rs.getDouble("price"));
                book.setCondition(rs.getString("condition"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Adaugă o carte nouă la anticariat
    public void addAntiqueBook(AntiqueBook book) {
        String sql = "INSERT INTO antique_books(title, author, published_year, price, condition) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getPublishedYear());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setString(5, book.getCondition());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteBookByTitle(String title) {
        String sql = "DELETE FROM antique_books WHERE title = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);

            int affectedRows = pstmt.executeUpdate();

            // Returnează 'true' dacă cel puțin un rând a fost șters
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBookById(int bookId) {
        // Comanda SQL care șterge înregistrarea pe baza ID-ului
        String sql = "DELETE FROM antique_books WHERE antique_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se setează ID-ul în comanda SQL pentru a evita SQL Injection
            pstmt.setInt(1, bookId);

            // Se execută comanda și se verifică dacă a fost șters vreun rând
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0; // Returnează 'true' dacă ștergerea a avut succes

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
