// src/main/java/library/ui/LibrarianMainFrame.java
package library.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Don't forget to import your models and handlers as you add functionality
// import library.model.Book;
// import library.util.BookXMLHandler;
// etc.

public class LibrarianMainFrame extends JFrame {

    // Define the baby pink color at the class level
    private final Color babyPink = new Color(255, 204, 204);

    public LibrarianMainFrame() {
        setTitle("Librarian Main Panel");
        setSize(1000, 700); // Adjust size as needed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes only this window
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout()); // Use BorderLayout for overall layout

        getContentPane().setBackground(babyPink); // Set the background of the frame

        // Add a header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(babyPink);
        JLabel welcomeLabel = new JLabel("Welcome, Librarian!");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Create a tabbed pane for different librarian sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(babyPink); // Set tabbed pane background
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14)); // Make tabs more visible
        add(tabbedPane, BorderLayout.CENTER);

        // --- Add your different functional panels as new tabs here ---
        // For now, let's add placeholder panels

        // Books Tab
        JPanel bookManagementPanel = createBookManagementPanel();
        tabbedPane.addTab("Book Management", null, bookManagementPanel, "Manage library books");
        tabbedPane.setBackgroundAt(0, babyPink.darker()); // Make tab header slightly darker pink

        // Borrowers Tab
        JPanel borrowerManagementPanel = createBorrowerManagementPanel();
        tabbedPane.addTab("Borrower Management", null, borrowerManagementPanel, "Manage library borrowers");
        tabbedPane.setBackgroundAt(1, babyPink.darker());

        // Loan Management Tab
        JPanel loanManagementPanel = createLoanManagementPanel();
        tabbedPane.addTab("Loan Management", null, loanManagementPanel, "Manage book loans and returns");
        tabbedPane.setBackgroundAt(2, babyPink.darker());

        // --- End of tabs ---

        // Add a logout button or other global actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(babyPink);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose(); // Close librarian frame
            new LoginFrame().setVisible(true); // Go back to login screen
        });
        footerPanel.add(logoutButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    // Placeholder methods for your future panels
    private JPanel createBookManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(babyPink.brighter()); // Slightly different shade for internal panels
        JLabel label = new JLabel("This is the Book Management Panel", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.PLAIN, 18));
        panel.add(label, BorderLayout.CENTER);
        // This is where you'll add your JTable for books, add/edit/delete buttons, search fields, etc.
        return panel;
    }

    private JPanel createBorrowerManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(babyPink.brighter());
        JLabel label = new JLabel("This is the Borrower Management Panel", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.PLAIN, 18));
        panel.add(label, BorderLayout.CENTER);
        // This is where you'll add your JTable for borrowers, add/edit/delete buttons, search fields, etc.
        return panel;
    }

    private JPanel createLoanManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(babyPink.brighter());
        JLabel label = new JLabel("This is the Loan Management Panel", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.PLAIN, 18));
        panel.add(label, BorderLayout.CENTER);
        // This is where you'll add your JTable for loans, issue/return buttons, overdue reports, etc.
        return panel;
    }

    // You can add a main method for testing this frame independently if needed
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibrarianMainFrame().setVisible(true);
        });
    }
}