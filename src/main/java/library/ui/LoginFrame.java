package library.ui;

import library.model.Borrower;
import library.model.Person;
import library.util.BorrowerXMLHandler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;// Pentru a compara obiecte în siguranță (ex: parole)
import java.awt.event.MouseAdapter; // Importăm MouseAdapter
import java.awt.event.MouseEvent;   // Importăm MouseEvent
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class LoginFrame extends JFrame {

    private final Color babyPink = new Color(255, 204, 204);
    private List<Borrower> allUsers = new ArrayList<>();
    private final Color focusBgColor = new Color(255, 255, 224);

    public LoginFrame() {
        setTitle("Library Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Când se apasă x, programul se oprește complet
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());// Folosește GridBagLayout pentru a aranja componentele într-o grilă
        getContentPane().setBackground(babyPink);


        loadUserData();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Username (ID):");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);// Folosim JPasswordField pentru a masca parola xxxxxxxx

        JButton loginButton = new JButton("Login");

        JButton bookstoreButton = new JButton("Explore Bookstore");
        bookstoreButton.addActionListener(e -> {
            new BookstoreFrame().setVisible(true);
        });

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

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(bookstoreButton, gbc);
        FocusAdapter focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(focusBgColor);
            }

            @Override
            public void focusLost(FocusEvent e) {
                e.getComponent().setBackground(babyPink);
            }
        };
        userField.addFocusListener(focusListener);
        passField.addFocusListener(focusListener);


        Color originalButtonColor = loginButton.getBackground();
        Color hoverButtonColor = originalButtonColor.brighter();

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                loginButton.setBackground(hoverButtonColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {

                loginButton.setBackground(originalButtonColor);
            }
        });

        loginButton.addActionListener(e -> {
            try {

                int userId = Integer.parseInt(userField.getText());
                String password = new String(passField.getPassword());


                Borrower authenticatedUser = allUsers.stream()
                        .filter(user -> user.getId() == userId && Objects.equals(user.getPassword(), password))
                        .findFirst()
                        .orElse(null);


                if (authenticatedUser != null) {

                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + authenticatedUser.getName());

                    // Navigarea pe bază de rol
                    if ("Librarian".equalsIgnoreCase(authenticatedUser.getRole())) {
                        new LibrarianMainFrame().setVisible(true);
                    } else {
                        new BorrowerMainFrame(authenticatedUser).setVisible(true);
                    }
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid ID or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadUserData() {
        try {
            File borrowersFile = new File("borrowers.xml");

            if (!borrowersFile.exists()) {
                JOptionPane.showMessageDialog(this, "Could not find borrowers.xml in the project root directory.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            InputStream borrowersStream = new FileInputStream(borrowersFile);
            allUsers = BorrowerXMLHandler.loadAll(borrowersStream);
            System.out.println("DEBUG (Login): Loaded " + allUsers.size() + " users from project root.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Data Load Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}