package library.util;
//Data Access Object
import library.model.AntiqueBook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntiqueBookDAO {

    // Metodă ajutătoare pentru a evita duplicarea codului
    private AntiqueBook mapResultSetToBook(ResultSet rs) throws SQLException {
        AntiqueBook book = new AntiqueBook();
        book.setId(rs.getInt("antique_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublishedYear(rs.getInt("published_year"));
        book.setPrice(rs.getDouble("price"));
        book.setCondition(rs.getString("condition"));
        // Nu este nevoie de calea imaginii pentru această funcționalitate
        return book;
    }

    public List<AntiqueBook> getAllAntiqueBooks() {
        List<AntiqueBook> books = new ArrayList<>();
        String sql = "SELECT * FROM antique_books";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    /**
     * NOU: Caută cărți după titlu sau autor.
     * @param searchTerm Termenul de căutare.
     * @return O listă de cărți care corespund căutării.
     */
    public List<AntiqueBook> searchBooks(String searchTerm) {
        List<AntiqueBook> books = new ArrayList<>();
        // Folosim LIKE pentru a căuta substring-uri
        String sql = "SELECT * FROM antique_books WHERE title LIKE ? OR author LIKE ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Adăugăm '%' pentru a căuta oriunde în string
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Metodele existente (sortare, ștergere) rămân aici...
    public List<AntiqueBook> getSortedAntiqueBooks(String sortBy, String sortOrder) {
        List<AntiqueBook> books = new ArrayList<>();
        String dbColumn;
        switch (sortBy) {
            case "Author": dbColumn = "author"; break;
            case "Year": dbColumn = "published_year"; break;
            case "Price": dbColumn = "price"; break;
            case "Condition": dbColumn = "condition"; break;
            default: dbColumn = "title"; break;
        }
        if (!"ASC".equalsIgnoreCase(sortOrder) && !"DESC".equalsIgnoreCase(sortOrder)) {
            sortOrder = "ASC";
        }
        String sql = "SELECT * FROM antique_books ORDER BY " + dbColumn + " " + sortOrder;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

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
}
