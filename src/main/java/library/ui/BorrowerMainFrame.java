package library.ui;

import library.model.Book;
import library.model.Borrower;
import library.model.Loan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;// Pentru a asculta click-uri de mouse
import java.awt.event.MouseEvent;// Obiectul evenimentului de mouse
import java.time.LocalDate;
import java.time.ZoneId;// Pentru a gestiona fusuri orare
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;// Pentru a gestiona erori la parsarea datelor
import java.time.temporal.ChronoUnit;// Pentru a calcula diferența dintre date (zile)
import java.util.*;// Importă diverse clase utilitare (List, ArrayList, etc.)
import java.util.List;
import java.util.stream.Collectors;

import com.toedter.calendar.JCalendar;// Importă toate clasele din biblioteca JCalendar
import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JDateChooser;

public class BorrowerMainFrame extends JFrame {

    private Borrower loggedInBorrower;
    private DefaultTableModel borrowedBooksModel;
    private JTable borrowedBooksTable;

    private JCalendar jCalendarComponent;
    private JDialog calendarDialog;

    private List<LocalDate> datesToMark = new ArrayList<>();
    private final Color babyPink = new Color(255, 204, 204);

    public BorrowerMainFrame(Borrower borrower) {
        this.loggedInBorrower = borrower;
        setTitle("Welcome, " + loggedInBorrower.getName() + "!");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// La închidere, eliberează doar resursele acestei ferestre, nu oprește toată aplicația
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(babyPink);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createBorrowedBooksPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);

        initializeCalendarComponents();
        populateDatesToMark();

        if (datesToMark.isEmpty()) {
            System.out.println("DEBUG (BorrowerMainFrame): No active due dates found to highlight on the calendar.");
        } else {
            System.out.println("DEBUG (BorrowerMainFrame): datesToMark list contains " + datesToMark.size() + " due dates.");
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel welcomeLabel = new JLabel("Your Borrowed Books, " + loggedInBorrower.getName());
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        headerPanel.add(welcomeLabel);
        headerPanel.setBackground(babyPink);
        return headerPanel;
    }

    private JScrollPane createBorrowedBooksPanel() {
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Borrow Date", "Due Date", "Days Left"};
        borrowedBooksModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        borrowedBooksTable = new JTable(borrowedBooksModel);

        borrowedBooksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = borrowedBooksTable.rowAtPoint(e.getPoint());
                int col = borrowedBooksTable.columnAtPoint(e.getPoint());
                if (row >= 0 && (col == 4 || col == 5)) {
                    Object cellValue = borrowedBooksTable.getValueAt(row, col);
                    if (cellValue instanceof String) {
                        displaySingleDateInCalendar((String) cellValue);
                    }
                }
            }
        });

        populateBorrowedBooksTable();

        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.getViewport().setBackground(babyPink);
        borrowedBooksTable.setBackground(babyPink);

        return scrollPane;
    }

    private JPanel createActionPanel() {
        // Panel now uses FlowLayout, aligning components to the center with a horizontal gap
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Added gaps for spacing
        actionPanel.setBackground(babyPink);

        // Button to view all due dates
        JButton viewBorrowedDatesButton = new JButton("View All Due Dates on Calendar");
        viewBorrowedDatesButton.addActionListener(e -> showAllBorrowedDatesOnCalendar());

        // NEW: Logout Button
        JButton logoutButton = new JButton("Logout & Return to Login");
        logoutButton.addActionListener(e -> {
            dispose(); // Closes the current BorrowerMainFrame window
            new LoginFrame().setVisible(true); // Creates and shows a new LoginFrame
        });

        // Add both buttons to the panel
        actionPanel.add(viewBorrowedDatesButton);
        actionPanel.add(logoutButton); // Add the new button

        return actionPanel;
    }
    private void populateBorrowedBooksTable() {
        borrowedBooksModel.setRowCount(0);

        List<Loan> borrowerLoans = loggedInBorrower.getBorrowedLoans();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Loan loan : borrowerLoans) {
            Book borrowedBook = loan.getLoanedBook();
            if (borrowedBook != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), loan.getDueDate());
                String daysLeftStr;
                if (!loan.isActive()) {
                    daysLeftStr = "Returned";
                } else if (daysLeft >= 0) {
                    daysLeftStr = daysLeft + " days";
                } else {
                    daysLeftStr = "OVERDUE by " + (-daysLeft) + " days";
                }

                borrowedBooksModel.addRow(new Object[]{
                        borrowedBook.getBookID(),
                        borrowedBook.getTitle(),
                        borrowedBook.getAuthor(),
                        borrowedBook.getGenre(),
                        loan.getIssuedDate().format(formatter),
                        loan.getDueDate().format(formatter),
                        daysLeftStr
                });
            }
        }
    }

    private void populateDatesToMark() {
        datesToMark.clear();


        List<Loan> borrowerLoans = loggedInBorrower.getBorrowedLoans();

        for (Loan loan : borrowerLoans) {
            if (loan.isActive()) {
                datesToMark.add(loan.getDueDate());
            }
        }
        System.out.println("DEBUG: Populated " + datesToMark.size() + " active due dates to mark.");


        if (jCalendarComponent != null) {
            jCalendarComponent.repaint();
        }
    }

    private void initializeCalendarComponents() {
        jCalendarComponent = new JCalendar();
        jCalendarComponent.setPreferredSize(new Dimension(400, 300));
        jCalendarComponent.setWeekOfYearVisible(false);

        jCalendarComponent.getDayChooser().addDateEvaluator(new IDateEvaluator() {
            @Override
            public boolean isSpecial(Date date) {
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return datesToMark.contains(localDate);
            }

            @Override
            public Color getSpecialForegroundColor() {
                return Color.BLACK;
            }

            @Override
            public Color getSpecialBackroundColor() {
                return babyPink;
            }

            @Override
            public String getSpecialTooltip() {
                return "Book is due on this date!";
            }

            @Override
            public boolean isInvalid(Date date) { return false; }
            @Override
            public Color getInvalidForegroundColor() { return null; }
            @Override
            public Color getInvalidBackroundColor() { return null; }
            @Override
            public String getInvalidTooltip() { return null; }
        });

        calendarDialog = new JDialog(this, "Due Dates Calendar", true);
        calendarDialog.setLayout(new BorderLayout());
        calendarDialog.add(jCalendarComponent, BorderLayout.CENTER);
        calendarDialog.getContentPane().setBackground(babyPink);
        jCalendarComponent.setBackground(babyPink);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> calendarDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        buttonPanel.setBackground(babyPink);
        calendarDialog.add(buttonPanel, BorderLayout.SOUTH);

        calendarDialog.pack();
        calendarDialog.setLocationRelativeTo(this);
    }

    private void showAllBorrowedDatesOnCalendar() {
        if (!datesToMark.isEmpty()) {
            jCalendarComponent.setCalendar(GregorianCalendar.from(datesToMark.get(0).atStartOfDay(ZoneId.systemDefault())));
        } else {
            jCalendarComponent.setCalendar(Calendar.getInstance());
        }
        calendarDialog.setVisible(true);
    }

    private void displaySingleDateInCalendar(String dateString) {
        try {
            LocalDate clickedLocalDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            Date dateToDisplay = Date.from(clickedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            JDateChooser singleDateChooser = new JDateChooser(dateToDisplay);
            singleDateChooser.setDateFormatString("dd-MM-yyyy");
            JDialog singleDateDialog = new JDialog(this, "Selected Date", true);
            singleDateDialog.add(singleDateChooser);
            singleDateDialog.getContentPane().setBackground(babyPink);
            singleDateChooser.setBackground(babyPink);
            singleDateDialog.pack();
            singleDateDialog.setLocationRelativeTo(this);
            singleDateDialog.setVisible(true);

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Error parsing date '" + dateString + "'.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}