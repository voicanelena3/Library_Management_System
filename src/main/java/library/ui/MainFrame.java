package library.ui;

import library.model.Borrower;
import library.util.BorrowerXMLHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class MainFrame extends JFrame {

    private DefaultTableModel borrowerModel;
    private JTable borrowerTable;
    private List<Borrower> borrowers = new ArrayList<>();

    private JTextField idField = new JTextField(10);
    private JTextField nameField = new JTextField(10);
    private JTextField passwordField = new JPasswordField(10);
    private JTextField addressField = new JTextField(10);
    private JTextField phoneField = new JTextField(10);
    private JTextField emailField = new JTextField(10);

    public MainFrame() {
        setTitle("Library Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Email", "Phone", "Borrowed Books"};
        borrowerModel = new DefaultTableModel(columnNames, 0); // Initialize here

        try {
            String filePath = "src/main/resources/data/borrowers.xml";
            File file = new File(filePath);

            if (file.exists()) {
                borrowers = BorrowerXMLHandler.loadAll(filePath);
                if (borrowers != null) {
                    for (Borrower b : borrowers) {
                        borrowerModel.addRow(new Object[]{
                                b.getId(), b.getName(), b.getEmail(), b.getPhoneNumber(), b.getBorrowedBooks().size()
                        });
                    }
                } else {
                    System.out.println("DEBUG: BorrowerXMLHandler.loadAll returned null. Initializing empty list.");
                    borrowers = new ArrayList<>();
                }
            } else {
                System.out.println("DEBUG: borrowers.xml does not exist at " + file.getAbsolutePath() + ". Initializing empty list.");
                borrowers = new ArrayList<>();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load borrowers from XML: " + e.getMessage(), "XML Load Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            borrowers = new ArrayList<>();
        }

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        formPanel.setBorder(BorderFactory.createTitledBorder("Borrower Information"));

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3; formPanel.add(nameField, gbc);

        gbc.gridx = 4; gbc.gridy = 0; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 5; formPanel.add(passwordField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; formPanel.add(addressField, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3; formPanel.add(phoneField, gbc);

        gbc.gridx = 4; gbc.gridy = 1; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 5; formPanel.add(emailField, gbc);

        return formPanel;
    }

    private JScrollPane createTablePanel() {
        borrowerTable = new JTable(borrowerModel);
        return new JScrollPane(borrowerTable);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Borrower");
        JButton saveAllButton = new JButton("Save All");

        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String password = new String(((JPasswordField)passwordField).getPassword());
                String address = addressField.getText();
                int phone = Integer.parseInt(phoneField.getText());
                String email = emailField.getText();

                Borrower b = new Borrower(id, name, password, address, phone, email, "Borrower");

                boolean idExists = borrowers.stream().anyMatch(borrower -> borrower.getId() == id);
                if (idExists) {
                    JOptionPane.showMessageDialog(this, "Borrower with this ID already exists!", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                borrowers.add(b);
                borrowerModel.addRow(new Object[]{
                        b.getId(), b.getName(), b.getEmail(), b.getPhoneNumber(), b.getBorrowedBooks().size()
                });

                idField.setText("");
                nameField.setText("");
                ((JPasswordField)passwordField).setText("");
                addressField.setText("");
                phoneField.setText("");
                emailField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Please enter valid numbers for ID and Phone.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding borrower: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        saveAllButton.addActionListener(e -> {
            try {
                // FIX: Use the correct method name saveAll() and the correct file path
                BorrowerXMLHandler.saveAll(borrowers, "src/main/resources/data/borrowers.xml");
                JOptionPane.showMessageDialog(this, "Saved " + borrowers.size() + " borrower(s) to XML.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving to XML: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(saveAllButton);

        return buttonPanel;
    }
}