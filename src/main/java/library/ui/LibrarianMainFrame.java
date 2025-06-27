package library.ui;

import library.model.Book;
import library.model.Borrower;
import library.model.Loan;
import library.util.BookXMLHandler;
import library.util.BorrowerXMLHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File; // Import File
import java.io.FileInputStream; // Import FileInputStream
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LibrarianMainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private DefaultTableModel bookTableModel;
    private DefaultTableModel borrowerTableModel;
    private DefaultTableModel loanTableModel;

    private List<Book> allBooks = new ArrayList<>();
    private List<Borrower> allBorrowers = new ArrayList<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final Color babyPink = new Color(255, 204, 204);

    // Define file paths as constants
    // private static final String BOOKS_FILE_PATH = "books.xml"; // No longer needed for loading
    private static final String BORROWERS_FILE_PATH = "borrowers.xml";


    public LibrarianMainFrame() {
        setTitle("Librarian Main Panel");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(babyPink);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAllData();
                dispose();
                System.exit(0);
            }
        });

        loadAllData();

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(babyPink.darker());

        JPanel bookManagementPanel = createBookManagementPanel();
        JPanel borrowerManagementPanel = createBorrowerManagementPanel();
        JPanel loanManagementPanel = createLoanManagementPanel();

        tabbedPane.addTab("Book Management", bookManagementPanel);
        tabbedPane.addTab("Borrower Management", borrowerManagementPanel);
        tabbedPane.addTab("Loan Management", loanManagementPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBackground(babyPink);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            saveAllData();
            new LoginFrame().setVisible(true);
            dispose();
        });
        southPanel.add(logoutButton);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadAllData() {
        try {
            // --- MODIFIED: Use a hardcoded list of books instead of loading from a file ---
            allBooks = new ArrayList<>();
            allBooks.add(new Book(104, "To Kill a Mockingbird", "Harper Lee", "Fiction"));
            allBooks.add(new Book(105, "The Great Gatsby", "F. Scott Fitzgerald", "Classic"));
            allBooks.add(new Book(106, "Dune", "Frank Herbert", "Science Fiction"));
            System.out.println("DEBUG: Loaded " + allBooks.size() + " hardcoded books.");

            // Load borrowers from the file as before
            File borrowersFile = new File(BORROWERS_FILE_PATH);
            if (borrowersFile.exists()) {
                allBorrowers = BorrowerXMLHandler.loadAll(BORROWERS_FILE_PATH);
            } else {
                JOptionPane.showMessageDialog(this, "borrowers.xml not found in project root. Starting with empty list.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Data Load Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void saveAllData() {
        try {
            // --- MODIFIED: Only save the borrowers file ---
            // The book list is now static and should not be saved.
            // BookXMLHandler.saveAll(allBooks, BOOKS_FILE_PATH);
            BorrowerXMLHandler.saveAll(allBorrowers, BORROWERS_FILE_PATH);
            System.out.println("Borrower data saved successfully to project root.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel createBookManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(babyPink.brighter());
        JLabel titleLabel = new JLabel("Book Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Title", "Author", "Genre", "Status"};
        bookTableModel = new DefaultTableModel(columnNames, 0);
        JTable bookTable = new JTable(bookTableModel);
        bookTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(bookTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        refreshBookTable();

        JPanel addFormPanel = new JPanel(new GridBagLayout());
        addFormPanel.setBorder(BorderFactory.createTitledBorder("Add New Book"));
        addFormPanel.setBackground(babyPink.brighter());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(15);
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JTextField genreField = new JTextField(15);
        JButton addButton = new JButton("Add Book to Library");

        gbc.gridx = 0; gbc.gridy = 0; addFormPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; addFormPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; addFormPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; addFormPanel.add(titleField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; addFormPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; addFormPanel.add(authorField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; addFormPanel.add(new JLabel("Genre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; addFormPanel.add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        addFormPanel.add(addButton, gbc);

        panel.add(addFormPanel, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            try {
                int bookID = Integer.parseInt(idField.getText());
                String title = titleField.getText();
                String author = authorField.getText();
                String genre = genreField.getText();

                if (title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All book fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (allBooks.stream().anyMatch(book -> book.getBookID() == bookID)) {
                    JOptionPane.showMessageDialog(this, "A book with this ID already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Book newBook = new Book(bookID, title, author, genre);
                allBooks.add(newBook);
                refreshBookTable();

                idField.setText("");
                titleField.setText("");
                authorField.setText("");
                genreField.setText("");

                JOptionPane.showMessageDialog(this, "Book added to the in-memory list. Changes will be lost on exit.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID for the book.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    private void refreshBookTable() {
        bookTableModel.setRowCount(0);
        for (Book book : allBooks) {
            bookTableModel.addRow(new Object[]{book.getBookID(), book.getTitle(), book.getAuthor(), book.getGenre(), book.isBorrowed() ? "Borrowed" : "Available"});
        }
    }

    private JPanel createBorrowerManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(babyPink.brighter());
        JLabel titleLabel = new JLabel("Borrower Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Email", "Phone", "Borrowed Count"};
        borrowerTableModel = new DefaultTableModel(columnNames, 0);
        JTable borrowerTable = new JTable(borrowerTableModel);
        borrowerTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(borrowerTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        refreshBorrowerTable();

        JPanel addFormPanel = new JPanel(new GridBagLayout());
        addFormPanel.setBorder(BorderFactory.createTitledBorder("Add New Borrower"));
        addFormPanel.setBackground(babyPink.brighter());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField addressField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JButton addButton = new JButton("Add Borrower");

        gbc.gridx = 0; gbc.gridy = 0; addFormPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; addFormPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; addFormPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; addFormPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; addFormPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; addFormPanel.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; addFormPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; addFormPanel.add(addressField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; addFormPanel.add(new JLabel("Phone No:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; addFormPanel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; addFormPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; addFormPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        addFormPanel.add(addButton, gbc);

        panel.add(addFormPanel, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();
                int phoneNo = Integer.parseInt(phoneField.getText());
                String email = emailField.getText();

                if (name.isEmpty() || password.isEmpty() || address.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All borrower fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (allBorrowers.stream().anyMatch(borrower -> borrower.getId() == id)) {
                    JOptionPane.showMessageDialog(this, "A user with this ID already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Borrower newBorrower = new Borrower(id, name, password, address, phoneNo, email);
                allBorrowers.add(newBorrower);
                refreshBorrowerTable();

                idField.setText("");
                nameField.setText("");
                passwordField.setText("");
                addressField.setText("");
                phoneField.setText("");
                emailField.setText("");

                JOptionPane.showMessageDialog(this, "Borrower added successfully! Remember to save on exit.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID and Phone No.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    private void refreshBorrowerTable() {
        borrowerTableModel.setRowCount(0);
        for (Borrower borrower : allBorrowers) {
            if (Objects.equals(borrower.getRole(), "Borrower")) {
                borrowerTableModel.addRow(new Object[]{
                        borrower.getId(),
                        borrower.getName(),
                        borrower.getEmail(),
                        borrower.getPhoneNumber(),
                        borrower.getBorrowedLoans().size()
                });
            }
        }
    }

    private JPanel createLoanManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(babyPink.brighter());
        JLabel titleLabel = new JLabel("Loan Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Borrower ID", "Borrower Name", "Book ID", "Book Title", "Issued Date", "Due Date", "Return Date", "Fine", "Fine Paid"};
        loanTableModel = new DefaultTableModel(columnNames, 0);
        JTable loanTable = new JTable(loanTableModel);
        loanTable.setFillsViewportHeight(true);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(loanTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        refreshLoanTable();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Loans"));
        formPanel.setBackground(babyPink.brighter());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField borrowerIdField = new JTextField(15);
        JTextField bookIdField = new JTextField(15);
        JButton issueButton = new JButton("Issue Book");
        JButton returnButton = new JButton("Return Selected Book");
        JButton payFineButton = new JButton("Pay Fine for Selected");

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Borrower ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(borrowerIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(bookIdField, gbc);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonsPanel.setBackground(babyPink.brighter());
        buttonsPanel.add(issueButton);
        buttonsPanel.add(returnButton);
        buttonsPanel.add(payFineButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(buttonsPanel, gbc);

        panel.add(formPanel, BorderLayout.EAST);

        issueButton.addActionListener(e -> {
            try {
                int borrowerId = Integer.parseInt(borrowerIdField.getText());
                int bookId = Integer.parseInt(bookIdField.getText());

                Borrower borrower = allBorrowers.stream()
                        .filter(b -> b.getId() == borrowerId && "Borrower".equals(b.getRole()))
                        .findFirst().orElse(null);
                if (borrower == null) {
                    JOptionPane.showMessageDialog(this, "Borrower not found with ID: " + borrowerId, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Book bookToLoan = allBooks.stream().filter(book -> book.getBookID() == bookId).findFirst().orElse(null);
                if (bookToLoan == null) {
                    JOptionPane.showMessageDialog(this, "Book not found with ID: " + bookId, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bookToLoan.isBorrowed()) {
                    JOptionPane.showMessageDialog(this, "Book is already on loan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int newRecordId = allBorrowers.stream().flatMap(b -> b.getBorrowedLoans().stream()).mapToInt(Loan::getRecordId).max().orElse(0) + 1;
                Loan newLoan = new Loan(newRecordId, bookToLoan, LocalDate.now());
                borrower.addLoan(newLoan);
                bookToLoan.setBorrowed(true);

                refreshLoanTable();
                refreshBookTable();

                borrowerIdField.setText("");
                bookIdField.setText("");
                JOptionPane.showMessageDialog(this, "Book issued to " + borrower.getName() + "!", "Loan Issued", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric IDs.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a loan to return.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int borrowerIdFromTable = (int) loanTableModel.getValueAt(selectedRow, 0);
            int bookIdFromTable = (int) loanTableModel.getValueAt(selectedRow, 2);

            Borrower borrower = allBorrowers.stream().filter(b -> b.getId() == borrowerIdFromTable).findFirst().orElse(null);
            if (borrower == null) return;

            Loan loanToReturn = borrower.getBorrowedLoans().stream()
                    .filter(loan -> loan.getLoanedBook().getBookID() == bookIdFromTable && loan.isActive())
                    .findFirst().orElse(null);

            if (loanToReturn == null) {
                JOptionPane.showMessageDialog(this, "Active loan not found or already returned.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            loanToReturn.setReturnedDate(LocalDate.now());
            loanToReturn.getLoanedBook().setBorrowed(false);

            double fine = loanToReturn.computeFine();
            String fineMessage = (fine > 0) ? String.format("A fine of $%.2f is due.", fine) : "No fine.";
            JOptionPane.showMessageDialog(this, "Book returned. " + fineMessage, "Success", JOptionPane.INFORMATION_MESSAGE);

            refreshLoanTable();
            refreshBookTable();
        });

        payFineButton.addActionListener(e -> {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a loan to pay the fine for.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int borrowerIdFromTable = (int) loanTableModel.getValueAt(selectedRow, 0);
            int bookIdFromTable = (int) loanTableModel.getValueAt(selectedRow, 2);

            Borrower borrower = allBorrowers.stream().filter(b -> b.getId() == borrowerIdFromTable).findFirst().orElse(null);
            if (borrower == null) return;

            Loan loanToPay = borrower.getBorrowedLoans().stream()
                    .filter(l -> l.getLoanedBook().getBookID() == bookIdFromTable)
                    .findFirst().orElse(null);

            if (loanToPay == null) return;

            if (loanToPay.isFinePaid()) {
                JOptionPane.showMessageDialog(this, "Fine already paid.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (loanToPay.computeFine() > 0) {
                loanToPay.payFine();
                refreshLoanTable();
                JOptionPane.showMessageDialog(this, "Fine of $" + String.format("%.2f", loanToPay.computeFine()) + " paid.", "Fine Paid", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No outstanding fine.", "No Fine", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    private void refreshLoanTable() {
        loanTableModel.setRowCount(0);
        for (Borrower borrower : allBorrowers) {
            for (Loan loan : borrower.getBorrowedLoans()) {
                loanTableModel.addRow(new Object[]{
                        borrower.getId(),
                        borrower.getName(),
                        loan.getLoanedBook().getBookID(),
                        loan.getLoanedBook().getTitle(),
                        loan.getIssuedDate().format(DATE_FORMATTER),
                        loan.getDueDate().format(DATE_FORMATTER),
                        loan.getReturnedDate() != null ? loan.getReturnedDate().format(DATE_FORMATTER) : "Not Returned",
                        "$" + String.format("%.2f", loan.computeFine()),
                        loan.isFinePaid() ? "Yes" : "No"
                });
            }
        }
    }
}
