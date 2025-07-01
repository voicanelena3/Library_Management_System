package library.model;

public class AntiqueBook {
    private int id;
    private String title;
    private String author;
    private int publishedYear;
    private double price;
    private String condition;

    // Constructori
    public AntiqueBook() {}

    public AntiqueBook(String title, String author, int publishedYear, double price, String condition) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.price = price;
        this.condition = condition;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getPublishedYear() { return publishedYear; }
    public void setPublishedYear(int publishedYear) { this.publishedYear = publishedYear; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
}
