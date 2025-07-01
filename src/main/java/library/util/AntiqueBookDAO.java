package library.util;

import library.model.AntiqueBook;
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
}
