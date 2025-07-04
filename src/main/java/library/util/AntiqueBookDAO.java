package library.util;

import library.model.AntiqueBook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntiqueBookDAO {

    // Metoda existentă
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

    /**
     * NOU: Obține cărțile sortate după o anumită coloană și ordine.
     * @param sortBy Coloana după care se sortează (ex: "Title", "Price").
     * @param sortOrder Ordinea sortării ("ASC" pentru crescător, "DESC" pentru descrescător).
     * @return O listă de cărți sortate.
     */
    public List<AntiqueBook> getSortedAntiqueBooks(String sortBy, String sortOrder) {
        List<AntiqueBook> books = new ArrayList<>();

        // Maparea numelor din UI la numele coloanelor din baza de date pentru siguranță
        String dbColumn;
        switch (sortBy) {
            case "Author":
                dbColumn = "author";
                break;
            case "Year":
                dbColumn = "published_year";
                break;
            case "Price":
                dbColumn = "price";
                break;
            case "Condition":
                dbColumn = "condition";
                break;
            case "Title":
            default:
                dbColumn = "title";
                break;
        }

        // Validare simplă pentru a preveni SQL Injection pe ordinea sortării
        if (!"ASC".equalsIgnoreCase(sortOrder) && !"DESC".equalsIgnoreCase(sortOrder)) {
            sortOrder = "ASC"; // Valoare implicită sigură
        }

        String sql = "SELECT * FROM antique_books ORDER BY " + dbColumn + " " + sortOrder;

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

    // Metoda existentă
    public boolean deleteBookById(int bookId) {
        String sql = "DELETE FROM antique_books WHERE antique_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metoda existentă
    public void addAntiqueBook(AntiqueBook book) {
        // ... codul tău existent pentru adăugare ...
    }
}
