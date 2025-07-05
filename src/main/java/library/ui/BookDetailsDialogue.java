package library.ui;

import library.model.AntiqueBook;
import library.util.AntiqueBookDAO;
import javax.swing.*;
import java.awt.*;
import java.net.URL; // Necesar pentru a încărca resurse

public class BookDetailsDialogue extends JDialog {

    public BookDetailsDialogue(BookstoreFrame parent, AntiqueBook book, AntiqueBookDAO dao) {
        super(parent, "Book Details", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //Se folosește HTML direct în textul JLabel (<html><b>...</b></html>) pentru a formata textul
        detailsPanel.add(new JLabel("<html><b>Title:</b> " + book.getTitle() + "</html>"));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(new JLabel("<html><b>Author:</b> " + book.getAuthor() + "</html>"));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(new JLabel("<html><b>Year:</b> " + book.getPublishedYear() + "</html>"));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(new JLabel("<html><b>Condition:</b> " + book.getCondition() + "</html>"));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel priceLabel = new JLabel(String.format("<html><b>Price:</b> <font color='blue'>€%.2f</font></html>", book.getPrice()));
        priceLabel.setFont(new Font("Serif", Font.BOLD, 16));
        detailsPanel.add(priceLabel);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton buyButton;

        try {

            URL iconUrl = BookDetailsDialogue.class.getClassLoader().getResource("icons/cart.png");

            if (iconUrl != null) {
                ImageIcon originalIcon = new ImageIcon(iconUrl);

                Image image = originalIcon.getImage();
                Image resizedImage = image.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(resizedImage);

                buyButton = new JButton("Buy Now", scaledIcon);

            } else {
                //Gestionarea Erorilor:Dacă iconița nu este găsită (iconUrl == null), în loc să crape, afișează un mesaj
                // de eroare foarte detaliat în consolă, explicând posibilele cauze
                // (cale greșită, folderul resources neconfigurat în IDE etc.) și continuă prin a crea butonul fără iconiță.
                System.err.println("--- DEBUGGING ICON ERROR ---");
                System.err.println("EROARE: Iconița 'cart.png' nu a putut fi găsită.");
                System.err.println("Cauze posibile:");
                System.err.println("1. Calea este greșită. Calea căutată este: 'icons/cart.png' (relativ la folderul 'resources').");
                System.err.println("2. Folderul 'resources' NU este marcat ca 'Resources Root' în IDE (IntelliJ, Eclipse, etc.).");
                System.err.println("   -> Click dreapta pe folderul 'resources' -> Mark Directory as -> Resources Root.");
                System.err.println("3. Numele fișierului este greșit (ex: 'Cart.png' în loc de 'cart.png'). Numele sunt case-sensitive.");
                System.err.println("-----------------------------");
                buyButton = new JButton("Buy Now");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            buyButton = new JButton("Buy Now");
        }

        buyButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buyButton.addActionListener(e -> {
            new PaymentDialogue(parent, book, dao).setVisible(true);
            dispose();
        });

        buttonPanel.add(buyButton);

        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
