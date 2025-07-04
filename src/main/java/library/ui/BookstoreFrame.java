package library.ui;

import library.model.AntiqueBook;
import library.util.AntiqueBookDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BookstoreFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private AntiqueBookDAO antiqueBookDAO;
    private List<AntiqueBook> currentBooks;

    public BookstoreFrame() {
        this.antiqueBookDAO = new AntiqueBookDAO();
        setTitle("Antique Bookstore");
        setSize(800, 600); // Mărit puțin pentru a face loc noilor controale
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panoul de sus (Titlu + Opțiuni de Sortare) ---
        JPanel topPanel = new JPanel(new BorderLayout());

        // Titlul principal
        JLabel titleLabel = new JLabel("Welcome to our Antique Bookstore!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Panoul pentru sortare
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sort Options"));

        sortPanel.add(new JLabel("Sort by:"));
        String[] sortOptions = {"Title", "Author", "Year", "Price", "Condition"};
        JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
        sortPanel.add(sortComboBox);

        JRadioButton ascRadioButton = new JRadioButton("Ascending", true);
        JRadioButton descRadioButton = new JRadioButton("Descending");
        ButtonGroup sortOrderGroup = new ButtonGroup();
        sortOrderGroup.add(ascRadioButton);
        sortOrderGroup.add(descRadioButton);
        sortPanel.add(ascRadioButton);
        sortPanel.add(descRadioButton);

        JButton sortButton = new JButton("Apply Sort");
        sortPanel.add(sortButton);
        topPanel.add(sortPanel, BorderLayout.CENTER);

        // --- Tabelul ---
        // (codul pentru tabel rămâne la fel ca înainte)
        String[] columnNames = {"Title", "Author", "Year", "Condition", "Price (€)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(255, 204, 204));
        table.setSelectionForeground(Color.BLACK);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        AntiqueBook selectedBook = currentBooks.get(selectedRow);
                        new BookDetailsDialogue(BookstoreFrame.this, selectedBook, antiqueBookDAO).setVisible(true);
                    }
                }
            }
        });

        // --- Butonul de întoarcere ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Return to Login");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        bottomPanel.add(backButton);

        // --- Adăugarea componentelor în fereastră ---
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Logica Butonului de Sortare ---
        sortButton.addActionListener(e -> {
            String sortBy = (String) sortComboBox.getSelectedItem();
            String sortOrder = ascRadioButton.isSelected() ? "ASC" : "DESC";
            loadSortedBookData(sortBy, sortOrder);
        });

        // Încărcarea datelor inițiale
        loadInitialBookData();
    }

    private void loadInitialBookData() {
        this.currentBooks = antiqueBookDAO.getAllAntiqueBooks();
        populateTableWithCurrentBooks();
    }

    private void loadSortedBookData(String sortBy, String sortOrder) {
        this.currentBooks = antiqueBookDAO.getSortedAntiqueBooks(sortBy, sortOrder);
        populateTableWithCurrentBooks();
    }

    private void populateTableWithCurrentBooks() {
        tableModel.setRowCount(0); // Golește tabelul
        if (currentBooks != null) {
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
    }

    public void refreshBookData() {
        loadInitialBookData(); // Reîncarcă datele nesortate după o cumpărare
    }
}
