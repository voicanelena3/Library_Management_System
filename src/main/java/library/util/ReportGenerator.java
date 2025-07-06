package library.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import library.model.Book;
import library.model.Borrower;
import library.model.Loan;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;


public class ReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public static void generateBooksReport(List<Book> books, String filePath) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Library Book Catalog Report", TITLE_FONT));
            document.add(new Paragraph(" ")); // Add some space

            PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 2, 2});
            table.setWidthPercentage(100);

            Stream.of("ID", "Title", "Author", "Genre", "Status")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            for (Book book : books) {
                table.addCell(String.valueOf(book.getBookID()));
                table.addCell(book.getTitle());
                table.addCell(book.getAuthor());
                table.addCell(book.getGenre());
                table.addCell(book.isBorrowed() ? "Borrowed" : "Available");
            }

            document.add(table);
            JOptionPane.showMessageDialog(null, "Book report generated successfully at:\n" + filePath, "Report Generated", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            document.close();
        }
    }


    public static void generateBorrowersReport(List<Borrower> borrowers, String filePath) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Registered Borrowers Report", TITLE_FONT));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 2, 1.5f});
            table.setWidthPercentage(100);

            Stream.of("ID", "Name", "Email", "Phone Number", "Loans")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setPhrase(new Phrase(headerTitle, HEADER_FONT));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            for (Borrower borrower : borrowers) {
                if ("Borrower".equals(borrower.getRole())) {
                    table.addCell(String.valueOf(borrower.getId()));
                    table.addCell(borrower.getName());
                    table.addCell(borrower.getEmail());
                    table.addCell(String.valueOf(borrower.getPhoneNumber()));
                    table.addCell(String.valueOf(borrower.getBorrowedLoans().size()));
                }
            }

            document.add(table);
            JOptionPane.showMessageDialog(null, "Borrowers report generated successfully at:\n" + filePath, "Report Generated", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            document.close();
        }
    }


    public static void generateLoansReport(List<Borrower> borrowers, String filePath) {
        Document document = new Document(PageSize.A4.rotate()); // Use landscape for more columns
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Full Loan History Report", TITLE_FONT));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{1.5f, 3, 3, 2, 2, 2, 1.5f, 1.5f});
            table.setWidthPercentage(100);

            Stream.of("Borrower Name", "Book Title", "Author", "Issued", "Due", "Returned", "Fine", "Fine Paid")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setPhrase(new Phrase(headerTitle, HEADER_FONT));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            for (Borrower borrower : borrowers) {
                for(Loan loan : borrower.getBorrowedLoans()) {
                    table.addCell(borrower.getName());
                    table.addCell(loan.getLoanedBook().getTitle());
                    table.addCell(loan.getLoanedBook().getAuthor());
                    table.addCell(loan.getIssuedDate().format(DATE_FORMATTER));
                    table.addCell(loan.getDueDate().format(DATE_FORMATTER));
                    table.addCell(loan.getReturnedDate() != null ? loan.getReturnedDate().format(DATE_FORMATTER) : "Not Returned");
                    table.addCell(String.format("$%.2f", loan.computeFine()));
                    table.addCell(loan.isFinePaid() ? "Yes" : "No");
                }
            }

            document.add(table);
            JOptionPane.showMessageDialog(null, "Loans report generated successfully at:\n" + filePath, "Report Generated", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            document.close();
        }
    }
}
