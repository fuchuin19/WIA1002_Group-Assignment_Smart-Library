/**
 * LibraryADT.java
 *
 * Interface defining the core contract for the Smart Library system.
 * Any class that implements this interface must provide all six library
 * management operations: adding books, searching, borrowing, returning,
 * viewing the full catalogue, and viewing the borrow history.
 *
 * Purpose : Enforces a consistent API and decouples the interface from
 *           the concrete implementation (SmartLibrary).
 */
public interface LibraryADT {

    /**
     * Adds a new book to the library catalogue.
     *
     * @param isbn   the unique integer ISBN identifier
     * @param title  the title of the book
     * @param author the author of the book
     */
    void addBook(int isbn, String title, String author);

    /**
     * Searches for a book by ISBN number or by a partial title keyword.
     * If the query can be parsed as an integer, an ISBN search is performed.
     * Otherwise, a case-insensitive partial title search is performed.
     *
     * @param query an ISBN number string, or a title keyword string
     */
    void searchBook(String query);

    /**
     * Marks a book as borrowed and records it in the borrow history stack.
     *
     * @param isbn the ISBN of the book to borrow
     */
    void borrowBook(int isbn);

    /**
     * Returns a borrowed book, removing it from the borrow history stack
     * and marking it as available again in the catalogue.
     *
     * @param isbn the ISBN of the book to return
     */
    void returnBook(int isbn);

    /**
     * Displays all books in the catalogue sorted by ISBN in ascending order,
     * along with availability statistics.
     */
    void viewAllBooks();

    /**
     * Displays the borrow history with the most recently borrowed book first
     * (LIFO order), along with the total number currently borrowed.
     */
    void viewHistory();
}
