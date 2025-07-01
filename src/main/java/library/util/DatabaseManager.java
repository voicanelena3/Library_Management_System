package library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:library.db";
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DATABASE_URL);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Eroare la conectarea cu baza de date!", "Eroare Bază de Date", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }
}
