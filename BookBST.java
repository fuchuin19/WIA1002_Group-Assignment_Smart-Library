/**
 * BookBST.java
 *
 * Implements a Binary Search Tree (BST) data structure for storing Book objects.
 * Books are keyed by their integer ISBN, which guarantees unique, ordered storage.
 *
 * Data Structure : Binary Search Tree (BST)
 * Ordering       : Books are sorted by ISBN — left child has smaller ISBN,
 *                  right child has larger ISBN.
 * Performance    : O(log n) average case for insert, search, and delete.
 *                  O(n) worst case when the tree degenerates into a linked list
 *                  (e.g., ISBNs inserted in sorted order).
 *
 * All internal fields and node class are private to enforce information hiding.
 */
import java.util.ArrayList;

public class BookBST {

    // ─────────────────────────────────────────────────────────────────────────
    // INNER NODE CLASS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Private inner class representing a single node in the BST.
     * Each node stores one Book and holds references to its left and right children.
     */
    private static class Node {
        Book book;
        Node left;
        Node right;

        /** Constructs a leaf node containing the given book. */
        Node(Book book) {
            this.book = book;
            this.left = null;
            this.right = null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FIELDS
    // ─────────────────────────────────────────────────────────────────────────

    /** The root node of the BST. Null when the tree is empty. */
    private Node root;

    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────────────

    /** Constructs an empty BookBST. */
    public BookBST() {
        root = null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INSERT
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Inserts a new book into the BST keyed by its ISBN.
     * If a book with the same ISBN already exists, prints a warning message
     * and does NOT insert the duplicate — the BST must contain unique ISBNs.
     *
     * Time Complexity: O(log n) average case, O(n) worst case (degenerate tree)
     *
     * @param isbn   the unique ISBN of the new book
     * @param title  the title of the new book
     * @param author the author of the new book
     */
    public void insert(int isbn, String title, String author) {
        if (search(isbn) != null) {
            System.out.println("ISBN already exists. Use a different ISBN.");
            return;
        }
        Book newBook = new Book(isbn, title, author);
        root = insertRec(root, newBook);
    }

    /**
     * Recursive helper for insert.
     * Navigates left (smaller ISBN) or right (larger ISBN) until an empty
     * position is found, then places a new leaf node there.
     *
     * @param node the current subtree root (null signals the insertion point)
     * @param book the Book to insert
     * @return the updated subtree root after insertion
     */
    private Node insertRec(Node node, Book book) {
        if (node == null) {
            return new Node(book);
        }
        if (book.getIsbn() < node.book.getIsbn()) {
            node.left = insertRec(node.left, book);
        } else if (book.getIsbn() > node.book.getIsbn()) {
            node.right = insertRec(node.right, book);
        }
        return node;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SEARCH BY ISBN
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Searches the BST for a book with the given ISBN using recursive traversal.
     * Returns the Book object if found, or null if no matching ISBN exists.
     *
     * Time Complexity: O(log n) average case, O(n) worst case (degenerate tree)
     *
     * @param isbn the ISBN to search for
     * @return the matching Book, or null if not found
     */
    public Book search(int isbn) {
        return searchRec(root, isbn);
    }

    /**
     * Recursive helper for search.
     * Navigates left if the target ISBN is smaller than the current node's ISBN,
     * right if larger, and returns the book when the ISBN matches.
     *
     * @param node the current subtree root
     * @param isbn the ISBN to search for
     * @return the matching Book, or null if the subtree is exhausted
     */
    private Book searchRec(Node node, int isbn) {
        if (node == null) {
            return null;
        }
        if (isbn == node.book.getIsbn()) {
            return node.book;
        }
        if (isbn < node.book.getIsbn()) {
            return searchRec(node.left, isbn);
        }
        return searchRec(node.right, isbn);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SEARCH BY TITLE
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Searches the entire BST for books whose title contains the given keyword.
     * The comparison is case-insensitive and supports partial matches, so
     * searching "potter" will match "Harry Potter and the Philosopher's Stone".
     * Note: this search is by title only — author names are not checked.
     *
     * Time Complexity: O(n) — must visit every node since BST order is by ISBN,
     * not by title
     *
     * @param keyword the search keyword (partial or full title, any case)
     * @return an ArrayList of all Book objects whose title contains the keyword
     */
    public ArrayList<Book> searchByTitle(String keyword) {
        ArrayList<Book> results = new ArrayList<>();
        searchByTitleRec(root, keyword.toLowerCase(), results);
        return results;
    }

    /**
     * Recursive helper for searchByTitle.
     * Performs a full pre-order traversal of the BST, adding any book whose
     * lower-cased title contains the lower-cased keyword.
     *
     * @param node    the current subtree root
     * @param keyword the lower-cased search keyword
     * @param results the list collecting matching books
     */
    private void searchByTitleRec(Node node, String keyword, ArrayList<Book> results) {
        if (node == null) {
            return;
        }
        if (node.book.getTitle().toLowerCase().contains(keyword)) {
            results.add(node.book);
        }
        searchByTitleRec(node.left, keyword, results);
        searchByTitleRec(node.right, keyword, results);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // IN-ORDER LIST
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns all books in the BST as an ArrayList sorted by ISBN in ascending
     * order. Achieved via a standard in-order traversal: left → root → right.
     * An empty list is returned when the tree contains no books.
     *
     * Time Complexity: O(n) — visits every node exactly once
     *
     * @return an ArrayList of all Book objects sorted by ISBN ascending
     */
    public ArrayList<Book> inOrderList() {
        ArrayList<Book> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    /**
     * Recursive helper for inOrderList.
     * Visits the left subtree first, appends the current node's book, then
     * visits the right subtree — producing an ascending ISBN-ordered sequence.
     *
     * @param node the current subtree root
     * @param list the list collecting books in order
     */
    private void inOrderRec(Node node, ArrayList<Book> list) {
        if (node == null) {
            return;
        }
        inOrderRec(node.left, list);
        list.add(node.book);
        inOrderRec(node.right, list);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Deletes the book with the given ISBN from the BST.
     * Handles three structural cases:
     *   1. Leaf node (no children)  — simply remove it.
     *   2. One child               — replace the node with its child.
     *   3. Two children            — replace the node's book data with that of
     *                               its in-order successor (smallest ISBN in the
     *                               right subtree), then delete the successor.
     * Returns false if no book with the given ISBN exists in the BST.
     *
     * Time Complexity: O(log n) average case, O(n) worst case (degenerate tree)
     *
     * @param isbn the ISBN of the book to delete
     * @return true if the book was found and deleted, false if ISBN not found
     */
    public boolean delete(int isbn) {
        if (search(isbn) == null) {
            return false;
        }
        root = deleteRec(root, isbn);
        return true;
    }

    /**
     * Recursive helper for delete.
     * Navigates to the target node and removes it, restructuring the tree
     * as required by BST deletion rules.
     *
     * @param node the current subtree root
     * @param isbn the ISBN of the book to delete
     * @return the updated subtree root after deletion
     */
    private Node deleteRec(Node node, int isbn) {
        if (node == null) {
            return null;
        }
        if (isbn < node.book.getIsbn()) {
            node.left = deleteRec(node.left, isbn);
        } else if (isbn > node.book.getIsbn()) {
            node.right = deleteRec(node.right, isbn);
        } else {
            // Target node found — apply BST deletion rules
            if (node.left == null) {
                return node.right;      // Case 1 & 2: no left child
            }
            if (node.right == null) {
                return node.left;       // Case 2: no right child
            }
            // Case 3: two children — replace with in-order successor
            Node successor = findMin(node.right);
            node.book = successor.book;
            node.right = deleteRec(node.right, successor.book.getIsbn());
        }
        return node;
    }

    /**
     * Finds the node with the minimum ISBN in the given subtree.
     * In a BST this is always the leftmost node.
     *
     * @param node the root of the subtree to search
     * @return the node with the smallest ISBN in the subtree
     */
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}
