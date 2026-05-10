/**
 * BorrowStack.java
 *
 * Implements a Stack data structure for tracking books that are currently
 * borrowed from the Smart Library. Uses java.util.Stack<Book> as the
 * underlying storage mechanism, wrapped inside this class to enforce
 * information hiding and provide a clean, domain-specific API.
 *
 * Data Structure : Stack (LIFO — Last-In, First-Out)
 * Internal field : private Stack<Book> stack
 *
 * The LIFO nature means the most recently borrowed book always appears
 * first when the borrow history is displayed. Because the library stores
 * the same Book object reference in both the BST and this stack, calling
 * setBorrowed() on a returned book automatically updates the BST copy too.
 */
import java.util.ArrayList;
import java.util.Stack;

public class BorrowStack {

    // ─────────────────────────────────────────────────────────────────────────
    // FIELDS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * The underlying Java stack storing borrowed Book objects.
     * Private to enforce information hiding — callers use only the
     * public methods of BorrowStack.
     */
    private Stack<Book> stack;

    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────────────

    /** Constructs an empty BorrowStack. */
    public BorrowStack() {
        stack = new Stack<>();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUSH
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Pushes a borrowed book onto the top of the stack.
     * The pushed book becomes the new "most recently borrowed" entry and
     * will appear first when the history is displayed.
     *
     * Time Complexity: O(1) — direct push to the top of the stack
     *
     * @param b the Book to push onto the borrow stack
     */
    public void push(Book b) {
        stack.push(b);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POP
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Removes and returns the book at the top of the stack (most recently
     * borrowed). Returns null safely if the stack is empty instead of
     * throwing an EmptyStackException.
     *
     * Time Complexity: O(1) — direct removal from the top of the stack
     *
     * @return the top Book, or null if the stack is empty
     */
    public Book pop() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.pop();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FIND AND REMOVE
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Searches the entire stack for a book with the matching ISBN, removes it,
     * and returns it. All other books remain in the stack in their original order.
     *
     * Algorithm: Pop items from the main stack into a temporary stack until
     * the target book is found or the stack is exhausted. Then restore all
     * temporarily-popped books back onto the main stack. Because popping
     * into temp reverses order, popping temp back reverses it again — so the
     * final stack order matches the original minus the removed book.
     *
     * Time Complexity: O(n) — may need to inspect every element in the stack
     *
     * @param isbn the ISBN of the book to find and remove
     * @return the removed Book, or null if no book with that ISBN is in the stack
     */
    public Book findAndRemove(int isbn) {
        Stack<Book> temp = new Stack<>();
        Book found = null;

        // Pop items into temp until the target ISBN is found
        while (!stack.isEmpty()) {
            Book top = stack.pop();
            if (top.getIsbn() == isbn) {
                found = top;
                break;
            }
            temp.push(top);
        }

        // Restore all temporarily removed books back to the main stack
        // (popping temp reverses back to the original order above the target)
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }

        return found;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TO DISPLAY LIST
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns an ArrayList of all books in LIFO order (most recently borrowed
     * first) WITHOUT modifying the original stack.
     *
     * Algorithm: Copies all stack elements into a temporary stack using addAll()
     * — which iterates from index 0 (bottom) to the top, preserving the same
     * internal order. Popping the temporary stack then yields elements from
     * top (most recent) to bottom (oldest), giving the desired LIFO display.
     *
     * Time Complexity: O(n) — copies and pops every element once
     *
     * @return an ArrayList of books ordered from most recently borrowed to oldest
     */
    public ArrayList<Book> toDisplayList() {
        ArrayList<Book> list = new ArrayList<>();

        // Copy the original stack (addAll preserves bottom-to-top order in temp)
        Stack<Book> temp = new Stack<>();
        temp.addAll(stack);

        // Popping temp gives top-to-bottom (most recent first) — LIFO display order
        while (!temp.isEmpty()) {
            list.add(temp.pop());
        }
        return list;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // IS EMPTY
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns true if no books are currently in the borrow stack.
     * Used to guard against displaying an empty history or popping an empty stack.
     *
     * Time Complexity: O(1)
     *
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
