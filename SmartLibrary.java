/**
 * SmartLibrary.java
 *
 * Main library management system implementing the LibraryADT interface.
 * Manages the book catalogue with a Binary Search Tree (BookBST) and
 * tracks borrow history with a Stack (BorrowStack) in LIFO order.
 *
 * Input strategy: all integer reads use nextLine() + Integer.parseInt()
 * in a try-catch(NumberFormatException) to avoid the Scanner buffer-stuck
 * bug caused by nextInt() leaving a trailing newline.
 *
 * Object identity: pushing a Book onto BorrowStack stores the SAME object
 * reference that lives in the BST node.  Calling setBorrowed() after
 * findAndRemove() therefore updates the BST copy automatically.
 *
 * Unicode policy: every non-ASCII character in a string literal is written
 * as a Java Unicode escape (backslash-u + four hex digits).  The source file
 * therefore contains only ASCII bytes and compiles with plain "javac *.java"
 * on any platform regardless of the OS default charset.
 */
import java.util.ArrayList;
import java.util.Scanner;

public class SmartLibrary implements LibraryADT {

    // -----------------------------------------------------------------
    // FIELDS
    // -----------------------------------------------------------------

    /** BST storing all books in the catalogue, keyed by ISBN. */
    private BookBST catalogue;

    /** Stack recording the borrow history in LIFO order. */
    private BorrowStack borrowHistory;

    /** Shared Scanner for all console input. Never closed inside this class. */
    private Scanner scanner;

    // -----------------------------------------------------------------
    // CONSTRUCTOR
    // -----------------------------------------------------------------

    /**
     * Constructs the SmartLibrary system.
     * Initialises the BST and borrow stack, then preloads six sample books.
     *
     * @param scanner the shared Scanner for reading user input
     */
    public SmartLibrary(Scanner scanner) {
        this.catalogue     = new BookBST();
        this.borrowHistory = new BorrowStack();
        this.scanner       = scanner;
        loadSampleData();
    }

    /**
     * Inserts six sample books directly into the BST catalogue.
     * Bypasses capitalizeWords because these are canonical titles.
     */
    private void loadSampleData() {
        catalogue.insert(1001, "Harry Potter and the Philosopher's Stone", "J.K. Rowling");
        catalogue.insert(1002, "The Great Gatsby", "F. Scott Fitzgerald");
        catalogue.insert(1003, "To Kill a Mockingbird", "Harper Lee");
        catalogue.insert(1004, "1984", "George Orwell");
        catalogue.insert(1005, "The Alchemist", "Paulo Coelho");
        catalogue.insert(1006, "Clean Code", "Robert C. Martin");
    }

    // -----------------------------------------------------------------
    // MAIN MENU LOOP
    // -----------------------------------------------------------------

    /**
     * Starts the interactive console menu loop.
     * Displays the menu, reads the user choice, and routes to the
     * appropriate handler. Runs until option 7 (Exit) is selected.
     */
    public void run() {
        while (true) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": handleAddBook();    break;
                case "2": handleSearchBook(); break;
                case "3": handleBorrowBook(); break;
                case "4": handleReturnBook(); break;
                case "5": viewAllBooks();     break;
                case "6": viewHistory();      break;
                case "7":
                    System.out.println("\nThank you for using Smart Library. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nInvalid option. Please enter a number between 1 and 7.");
            }
        }
    }

    /**
     * Prints the main menu using double-line box-drawing characters.
     * All non-ASCII code points use Java Unicode escapes:
     *   \u2554 top-left  \u2550 horizontal  \u2557 top-right
     *   \u2551 vertical  \u2560 left-T     \u2563 right-T
     *   \u255A bot-left  \u255D bot-right  \uD83D\uDCDA books emoji
     */
    private void displayMenu() {
        // 38 horizontal-bar characters per row
        String bar = "\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550";

        System.out.println("");
        System.out.println("\u2554" + bar + "\u2557");
        System.out.println("\u2551         SMART LIBRARY SYSTEM         \u2551");
        System.out.println("\u2560" + bar + "\u2563");
        System.out.println("\u2551  1. Add New Book                     \u2551");
        System.out.println("\u2551  2. Search Book (ISBN or Title)      \u2551");
        System.out.println("\u2551  3. Borrow Book                      \u2551");
        System.out.println("\u2551  4. Return Book                      \u2551");
        System.out.println("\u2551  5. View All Books                   \u2551");
        System.out.println("\u2551  6. View Borrow History              \u2551");
        System.out.println("\u2551  7. Exit                             \u2551");
        System.out.println("\u255A" + bar + "\u255D");
        System.out.print("Enter your choice: ");
    }

    // -----------------------------------------------------------------
    // PRESENTATION LAYER
    // -----------------------------------------------------------------

    /**
     * Handles the Add New Book flow.
     * Validates ISBN (must be a positive integer), title (non-empty),
     * and author (non-empty). Capitalises words before saving.
     * Any validation failure returns to the menu without crashing.
     */
    private void handleAddBook() {
        System.out.print("\nEnter ISBN (numbers only): ");
        String isbnInput = scanner.nextLine().trim();
        int isbn;
        try {
            isbn = Integer.parseInt(isbnInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ISBN. Please enter numbers only.");
            return;
        }
        if (isbn <= 0) {
            System.out.println("ISBN must be a positive number.");
            return;
        }
        if (catalogue.search(isbn) != null) {
            System.out.println("ISBN already exists. Use a different ISBN.");
            return;
        }
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("Author cannot be empty.");
            return;
        }
        addBook(isbn, capitalizeWords(title), capitalizeWords(author));
    }

    /**
     * Handles the Search Book flow: prompts for a query and delegates
     * to searchBook() which auto-detects ISBN vs. title keyword.
     */
    private void handleSearchBook() {
        System.out.print("\nEnter ISBN number OR book title to search: ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("Search query cannot be empty.");
            return;
        }
        searchBook(query);
    }

    /**
     * Handles the Borrow Book flow: prompts for an ISBN (validated as
     * integer) and delegates to borrowBook().
     */
    private void handleBorrowBook() {
        System.out.print("\nEnter ISBN of book to borrow: ");
        String input = scanner.nextLine().trim();
        int isbn;
        try {
            isbn = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ISBN. Please enter numbers only.");
            return;
        }
        borrowBook(isbn);
    }

    /**
     * Handles the Return Book flow: prompts for an ISBN (validated as
     * integer) and delegates to returnBook().
     */
    private void handleReturnBook() {
        System.out.print("\nEnter ISBN of book to return: ");
        String input = scanner.nextLine().trim();
        int isbn;
        try {
            isbn = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ISBN. Please enter numbers only.");
            return;
        }
        returnBook(isbn);
    }

    // -----------------------------------------------------------------
    // UTILITY
    // -----------------------------------------------------------------

    /**
     * Capitalizes the first letter of each word and lowercases the rest.
     * Consecutive spaces collapse to one. Example: "harry POTTER" -> "Harry Potter".
     *
     * @param text the raw input string
     * @return the title-cased string trimmed of leading/trailing whitespace
     */
    private String capitalizeWords(String text) {
        String[] words = text.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1).toLowerCase());
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    // -----------------------------------------------------------------
    // LIBRARYADT IMPLEMENTATIONS
    // -----------------------------------------------------------------

    /**
     * Adds a new book to the BST catalogue.
     * Rejects duplicate ISBNs. On success prints a confirmation with the
     * full book details. Uses \u2713 (U+2713 check mark) as success prefix.
     *
     * @param isbn   unique positive ISBN
     * @param title  already-capitalised title
     * @param author already-capitalised author
     */
    @Override
    public void addBook(int isbn, String title, String author) {
        if (catalogue.search(isbn) != null) {
            System.out.println("ISBN already exists. Use a different ISBN.");
            return;
        }
        catalogue.insert(isbn, title, author);
        Book added = catalogue.search(isbn);
        System.out.println("\u2713 Book added successfully! " + added);
    }

    /**
     * Searches by ISBN (if query parses as int) or by partial title keyword
     * (case-insensitive). Title search is against the book title only --
     * searching "george" finds nothing because "George Orwell" is an author.
     *
     * @param query ISBN number string or partial title keyword
     */
    @Override
    public void searchBook(String query) {
        try {
            int isbn = Integer.parseInt(query);
            Book book = catalogue.search(isbn);
            if (book != null) {
                System.out.println("\nBook found:");
                System.out.println("  " + book);
            } else {
                System.out.println("No book found with ISBN: " + isbn);
            }
        } catch (NumberFormatException e) {
            ArrayList<Book> results = catalogue.searchByTitle(query);
            if (!results.isEmpty()) {
                System.out.println("\nFound " + results.size()
                        + " result(s) for \"" + query + "\":");
                for (Book b : results) {
                    System.out.println("  " + b);
                }
                System.out.println("(Note: search matches book title only, not author name)");
            } else {
                System.out.println("No books found matching '" + query + "'");
                System.out.println("(Note: search matches book title only, not author name)");
            }
        }
    }

    /**
     * Borrows a book: verifies it exists and is available, sets isBorrowed = true,
     * and pushes it onto the BorrowStack.
     * Uses \u2713 (U+2713 check mark) as success prefix.
     *
     * @param isbn the ISBN of the book to borrow
     */
    @Override
    public void borrowBook(int isbn) {
        Book book = catalogue.search(isbn);
        if (book == null) {
            System.out.println("Book with ISBN " + isbn + " not found in catalogue.");
            return;
        }
        if (book.isBorrowed()) {
            System.out.println("Sorry, '" + book.getTitle()
                    + "' is already borrowed by someone else.");
            return;
        }
        book.setBorrowed(true);
        borrowHistory.push(book);
        System.out.println("\u2713 Successfully borrowed: " + book.getTitle());
    }

    /**
     * Returns a borrowed book: calls findAndRemove() on the BorrowStack,
     * sets isBorrowed = false on the found object (which also updates the BST
     * copy via shared reference).
     * Uses \u2713 (U+2713 check mark) as success prefix.
     *
     * @param isbn the ISBN of the book to return
     */
    @Override
    public void returnBook(int isbn) {
        Book book = borrowHistory.findAndRemove(isbn);
        if (book == null) {
            System.out.println("This book is not in the borrow list. Cannot return.");
            return;
        }
        book.setBorrowed(false);
        System.out.println("\u2713 Successfully returned: " + book.getTitle()
                + ". It is now available.");
    }

    /**
     * Displays all books sorted by ISBN ascending (BST in-order traversal).
     * Prints a footer with total, available, and borrowed counts.
     */
    @Override
    public void viewAllBooks() {
        ArrayList<Book> books = catalogue.inOrderList();
        if (books.isEmpty()) {
            System.out.println("\nNo books in catalogue.");
            return;
        }
        System.out.println("\n=== Library Catalogue (Sorted by ISBN) ===");
        int available = 0, borrowed = 0;
        for (Book b : books) {
            System.out.println("  " + b);
            if (b.isBorrowed()) borrowed++; else available++;
        }
        System.out.println("-------------------------------------------");
        System.out.println("Total books: " + books.size()
                + " | Available: " + available
                + " | Borrowed: "  + borrowed);
    }

    /**
     * Displays the borrow history in LIFO order (most recent first).
     * Uses toDisplayList() which snapshots the stack without modifying it.
     * Prints a footer with the total currently borrowed count.
     */
    @Override
    public void viewHistory() {
        ArrayList<Book> history = borrowHistory.toDisplayList();
        if (history.isEmpty()) {
            System.out.println("\nNo borrow history yet.");
            return;
        }
        System.out.println("\n=== Borrow History (Most Recent First) ===");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + history.get(i));
        }
        System.out.println("------------------------------------------");
        System.out.println("Total currently borrowed: " + history.size());
    }
}
