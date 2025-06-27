package library.ui;

import library.model.Borrower;
import library.model.Person;
import library.util.BorrowerXMLHandler;

import javax.swing.*;
import java.awt.*;
import java.io.File; // Import the File class
import java.io.FileInputStream; // Import the FileInputStream class
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginFrame extends JFrame {

    private final Color babyPink = new Color(255, 204, 204);
    private List<Borrower> allUsers = new ArrayList<>();

    public LoginFrame() {
        setTitle("Library Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(babyPink);

        // Load user data
        loadUserData();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Username (ID):");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        // --- THE CORE LOGIN LOGIC ---
        loginButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userField.getText());
                String password = new String(passField.getPassword());

                // Find the user by ID and password
                Borrower authenticatedUser = allUsers.stream()
                        .filter(user -> user.getId() == userId && Objects.equals(user.getPassword(), password))
                        .findFirst()
                        .orElse(null);

                if (authenticatedUser != null) {
                    // Successful login, now check the role
                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + authenticatedUser.getName());

                    // --- ROLE-BASED NAVIGATION ---
                    if ("Librarian".equalsIgnoreCase(authenticatedUser.getRole())) {
                        // If user is a Librarian, open the Librarian frame
                        new LibrarianMainFrame().setVisible(true);
                    } else {
                        // Otherwise, assume Borrower and open the Borrower frame
                        new BorrowerMainFrame(authenticatedUser).setVisible(true);
                    }
                    dispose(); // Close the login window

                } else {
                    // Failed login
                    JOptionPane.showMessageDialog(this, "Invalid ID or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadUserData() {
        try {
            // MODIFIED: Load the file directly from the project root directory
            File borrowersFile = new File("borrowers.xml");

            if (!borrowersFile.exists()) {
                JOptionPane.showMessageDialog(this, "Could not find borrowers.xml in the project root directory.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use a FileInputStream to read the file from the specific path
            InputStream borrowersStream = new FileInputStream(borrowersFile);
            allUsers = BorrowerXMLHandler.loadAll(borrowersStream);
            System.out.println("DEBUG (Login): Loaded " + allUsers.size() + " users from project root.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Data Load Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
