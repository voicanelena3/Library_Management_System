// File: library/ui/PaymentDialog.java
package library.ui;

import library.model.AntiqueBook;
import library.util.AntiqueBookDAO;
import javax.swing.*;
import java.awt.*;

public class PaymentDialogue extends JDialog {

    public PaymentDialogue(BookstoreFrame parent, AntiqueBook book, AntiqueBookDAO dao) {
        super(parent, "Payment Details", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Card Number:"));
        formPanel.add(new JTextField());
        formPanel.add(new JLabel("Cardholder Name:"));
        formPanel.add(new JTextField());
        formPanel.add(new JLabel("Expiry Date (MM/YY):"));
        formPanel.add(new JTextField());
        formPanel.add(new JLabel("CVV:"));
        formPanel.add(new JPasswordField());

        JButton confirmButton = new JButton("Confirm Purchase");
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmButton.addActionListener(e -> {
            // Într-o aplicație reală, aici ar fi validarea datelor

            // Șterge cartea din baza de date
            boolean success = dao.deleteBookByTitle(book.getTitle()); // Vom folosi titlul ca identificator unic

            if (success) {
                JOptionPane.showMessageDialog(this, "Purchase successful! Thank you.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Reîmprospătează tabelul din fereastra principală
                parent.refreshBookData();

                // Închide fereastra de plată
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error processing purchase.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}