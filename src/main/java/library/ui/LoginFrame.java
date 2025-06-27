package library.ui;

import library.model.Borrower;
import library.util.BorrowerXMLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class LoginFrame extends JFrame {

    private JTextField idField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public LoginFrame() {
        setTitle("Library Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Use a soft, welcoming color for the background
        Color softBlue = new Color(220, 230, 240);
        getContentPane().setBackground(softBlue);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(softBlue);
        JLabel titleLabel = new JLabel("Welcome to the Library System");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main form panel with GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(softBlue);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel idLabel = new JLabel("User ID:");
        idField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Borrower", "Librarian"};
        roleComboBox = new JComboBox<>(roles);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Add components to the panel using GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(idLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action Listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        try {
            int userId = Integer.parseInt(idField.getText());
            String password = new String(passwordField.getPassword());
            String selectedRole = (String) roleComboBox.getSelectedItem();

            if (selectedRole == null) {
                JOptionPane.showMessageDialog(this, "Please select a role.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- CORRECTED LOGIC: Use a robust classpath loader for the XML file ---
            InputStream xmlStream = getClass().getClassLoader().getResourceAsStream("data/borrowers.xml");
            List<Borrower> allUsers = BorrowerXMLHandler.loadAll(xmlStream);

            // --- DEBUG PRINT: Check if data was loaded ---
            System.out.println("Loaded " + allUsers.size() + " users from borrowers.xml");

            Borrower loggedInUser = null;
            for (Borrower user : allUsers) {
                // --- DEBUG PRINT: Check each user's data from XML during the search ---
                System.out.println("Checking XML User: ID='" + user.getId() + "', Role='" + user.getRole() + "', Password='" + user.getPassword() + "'");

                // Compare values
                boolean idMatches = (user.getId() == userId);
                boolean passwordMatches = user.getPassword().equals(password);
                boolean roleMatches = Objects.equals(user.getRole(), selectedRole);

                System.out.println("  - ID Match: " + idMatches + ", Password Match: " + passwordMatches + ", Role Match: " + roleMatches);

                if (idMatches && passwordMatches && roleMatches) {
                    loggedInUser = user;
                    System.out.println("MATCH FOUND! Logging in...");
                    break;
                }
            }

            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close the login window

                if ("Borrower".equals(selectedRole)) {
                    // Open Borrower's main frame
                    BorrowerMainFrame borrowerFrame = new BorrowerMainFrame(loggedInUser);
                    borrowerFrame.setVisible(true);
                } else if ("Librarian".equals(selectedRole)) {
                    // Open Librarian's main frame
                    new LibrarianMainFrame().setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or role. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                System.out.println("No matching user found in the XML file.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric User ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}