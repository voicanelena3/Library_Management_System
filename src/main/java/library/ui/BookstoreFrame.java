package library.ui;

import library.model.AntiqueBook;
import library.util.AntiqueBookDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BookstoreFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private AntiqueBookDAO antiqueBookDAO;
    private List<AntiqueBook> currentBooks; // Store the currently displayed list of books

    public BookstoreFrame() {
        this.antiqueBookDAO = new AntiqueBookDAO();
        setTitle("Antique Bookstore");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Title Label ---
        JLabel titleLabel = new JLabel("Welcome to our Antique Bookstore!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Table Setup ---
        String[] columnNames = {"Title", "Author", "Year", "Condition", "Price (€)"};
        // Make table cells non-editable
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only one row to be selected

        // --- SET SELECTION COLOR TO PINK ---
        table.setSelectionBackground(new Color(255, 204, 204)); // Setează culoarea de selecție la roz
        table.setSelectionForeground(Color.BLACK); // Asigură că textul rămâne negru și lizibil

        // --- MOUSE LISTENER TO OPEN DETAILS WINDOW ---
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // This checks for a DOUBLE-CLICK.
                // If you want a SINGLE-CLICK, change e.getClickCount() == 2 to e.getClickCount() == 1
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Get the selected book from our stored list
                        AntiqueBook selectedBook = currentBooks.get(selectedRow);

                        // Open the details dialog for the selected book
                        new BookDetailsDialogue(BookstoreFrame.this, selectedBook, antiqueBookDAO).setVisible(true);
                    }
                }
            }
        });

        // --- Back Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Return to Login");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(backButton);

        // --- Add components to frame ---
        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load and display data
        loadBookData();
    }

    private void loadBookData() {
        // Store the books in the class-level list
        this.currentBooks = antiqueBookDAO.getAllAntiqueBooks();

        // Clear the table before adding new data
        tableModel.setRowCount(0);

        for (AntiqueBook book : currentBooks) {
            tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublishedYear(),
                    book.getCondition(),
                    String.format("%.2f", book.getPrice())
            });
        }
    }

    /**
     * This public method allows other windows (like the PaymentDialog)
     * to refresh the data in this table after a book is purchased.
     */
    public void refreshBookData() {
        loadBookData();
    }


}
