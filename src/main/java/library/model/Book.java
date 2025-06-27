// In file: src/main/java/library/model/Book.java
package library.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "bookID")
    private int bookID;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "author")
    private String author;
    @XmlElement(name = "genre")
    private String genre;
    @XmlElement(name = "isBorrowed")
    private boolean isBorrowed; // Indicates if the book is currently borrowed

    public Book() {
        // JAXB requires a no-argument constructor
    }

    public Book(int bookID, String title, String author, String genre) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isBorrowed = false; // Default status is available
    }

    // --- Getters ---
    public int getBookID() { return bookID; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public boolean isBorrowed() { return isBorrowed; }

    // --- Setters ---
    public void setBookID(int bookID) { this.bookID = bookID; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    @Override
    public String toString() {
        return "Book{" +
                "bookID=" + bookID +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", isBorrowed=" + isBorrowed +
                '}';
    }
}