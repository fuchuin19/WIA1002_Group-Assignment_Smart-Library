/**
 * Book.java
 *
 * Represents a single book entity in the Smart Library system.
 * Stores the book's ISBN, title, author, and current borrow status.
 *
 * All fields are private to enforce information hiding. External access
 * is provided through public getters and a setter for the borrow status.
 * The isBorrowed flag defaults to false (Available) on creation and is
 * updated by SmartLibrary when a book is borrowed or returned.
 */
public class Book {

    /** Unique integer identifier for the book. */
    private int isbn;

    /** Full title of the book. */
    private String title;

    /** Name of the book's author. */
    private String author;

    /**
     * Current borrow status. true = Borrowed, false = Available.
     * Defaults to false when a Book object is created.
     */
    private boolean isBorrowed;

    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Constructs a new Book with the given details.
     * The isBorrowed status is initialised to false (Available).
     *
     * @param isbn   the unique integer ISBN
     * @param title  the book's title
     * @param author the book's author
     */
    public Book(int isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GETTERS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns the book's ISBN.
     *
     * @return the integer ISBN
     */
    public int getIsbn() {
        return isbn;
    }

    /**
     * Returns the book's title.
     *
     * @return the title string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the book's author.
     *
     * @return the author string
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the current borrow status of the book.
     *
     * @return true if the book is currently borrowed, false if available
     */
    public boolean isBorrowed() {
        return isBorrowed;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SETTER
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Updates the borrow status of the book.
     * Called by SmartLibrary when a book is borrowed (true) or returned (false).
     *
     * @param borrowed true to mark as Borrowed, false to mark as Available
     */
    public void setBorrowed(boolean borrowed) {
        this.isBorrowed = borrowed;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TO STRING
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns a neatly formatted string representation of the book.
     * Format: [ISBN: 1001 | Title: Harry Potter | Author: J.K. Rowling | Status: Available]
     * The Status field displays "Available" or "Borrowed" based on isBorrowed.
     *
     * @return formatted book details string
     */
    @Override
    public String toString() {
        String status = isBorrowed ? "Borrowed" : "Available";
        return "[ISBN: " + isbn
                + " | Title: " + title
                + " | Author: " + author
                + " | Status: " + status + "]";
    }
}
