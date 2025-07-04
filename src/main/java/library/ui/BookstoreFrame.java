package library.ui;

import library.model.AntiqueBook;
import library.util.AntiqueBookDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private JTextField searchField; // NOU: Câmpul de căutare

    public BookstoreFrame() {
        this.antiqueBookDAO = new AntiqueBookDAO();
        setTitle("Antique Bookstore");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panoul de sus pentru titlu și controale
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JLabel titleLabel = new JLabel("Welcome to our Antique Bookstore!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Panou pentru controale (Căutare și Sortare)
        JPanel controlsPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        // NOU: Panoul pentru Căutare
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(new JLabel("Search by Title/Author:"));
        searchField = new JTextField(25);
        searchPanel.add(searchField);
        controlsPanel.add(searchPanel);

        // Panoul existent pentru Sortare
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
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
        controlsPanel.add(sortPanel);

        topPanel.add(controlsPanel, BorderLayout.CENTER);

        // Tabelul (cod existent)
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
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
                    AntiqueBook selectedBook = currentBooks.get(modelRow);
                    new BookDetailsDialogue(BookstoreFrame.this, selectedBook, antiqueBookDAO).setVisible(true);
                }
            }
        });

        // Butonul de întoarcere (cod existent)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Return to Login");
        bottomPanel.add(backButton);
        backButton.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        // Adăugarea componentelor în fereastră
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Logica pentru controale
        sortButton.addActionListener(e -> {
            String sortBy = (String) sortComboBox.getSelectedItem();
            String sortOrder = ascRadioButton.isSelected() ? "ASC" : "DESC";
            loadSortedBookData(sortBy, sortOrder);
        });

        // NOU: Listener pentru căutare în timp real
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });

        // Încărcarea datelor inițiale
        loadInitialBookData();
    }

    // NOU: Metoda care execută căutarea
    private void performSearch() {
        String searchTerm = searchField.getText();
        if (searchTerm.trim().isEmpty()) {
            loadInitialBookData();
        } else {
            this.currentBooks = antiqueBookDAO.searchBooks(searchTerm);
            populateTableWithCurrentBooks();
        }
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
        tableModel.setRowCount(0);
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
        String searchTerm = searchField.getText();
        // Reîmprospătează pe baza vizualizării curente (rezultatele căutării sau toate cărțile)
        if (searchTerm.trim().isEmpty()) {
            loadInitialBookData();
        } else {
            performSearch();
        }
    }
}
