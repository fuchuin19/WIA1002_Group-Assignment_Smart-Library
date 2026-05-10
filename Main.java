/**
 * Main.java
 *
 * Entry point for the Smart Library console application.
 * Creates a single shared Scanner and a SmartLibrary instance, then
 * hands control to the interactive menu loop.
 *
 * The Scanner is created here (owned by Main) and passed into SmartLibrary
 * so that all user input flows through one Scanner — avoiding the resource
 * conflict that arises when multiple Scanners wrap System.in.
 * The Scanner is intentionally not closed here; System.exit(0) inside
 * SmartLibrary.run() handles JVM shutdown cleanly.
 *
 * Compile : javac *.java
 * Run     : java Main
 */
import java.util.Scanner;

public class Main {

    /**
     * Application entry point.
     * Initialises the library system and starts the interactive console menu.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SmartLibrary library = new SmartLibrary(scanner);
        library.run();
    }
}
