package library.ui;

import library.model.AntiqueBook;
import library.util.AntiqueBookDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookstoreFrame extends JFrame {
    private DefaultTableModel tableModel;
    private AntiqueBookDAO antiqueBookDAO;

    public BookstoreFrame() {
        this.antiqueBookDAO = new AntiqueBookDAO();
        setTitle("Antique Bookstore");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Se închide doar această fereastră
        setLocationRelativeTo(null);

        // Crearea tabelului
        String[] columnNames = {"Title", "Author", "Year", "Condition", "Price (€)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);

        // Adăugăm un titlu
        JLabel titleLabel = new JLabel("Welcome to our Antique Bookstore!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adăugăm componentele în fereastră
        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Încărcăm și afișăm datele
        loadBookData();
    }

    private void loadBookData() {
        List<AntiqueBook> books = antiqueBookDAO.getAllAntiqueBooks();
        tableModel.setRowCount(0); // Golește tabelul înainte de a adăuga date noi
        for (AntiqueBook book : books) {
            tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublishedYear(),
                    book.getCondition(),
                    String.format("%.2f", book.getPrice())
            });
        }
    }
}