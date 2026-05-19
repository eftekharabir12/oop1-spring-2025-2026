// Declares that this class belongs to the root management system package
package pharmacymanagementsystem;

// Import the GUI class that builds and shows the main window
import pharmacymanagementsystem.gui.PharmacyGUI;

// Import the file I/O class so we can ensure the database file exists at startup
import pharmacymanagementsystem.fileio.MedicineFileIO;

// Import IOException for handling file-creation errors
import java.io.IOException;

/**
 * Start - Application entry point for the Pharmacy Management System.
 *
 * Responsibilities:
 *   1. Ensure the medicines.txt data file exists before the GUI opens.
 *   2. Launch the PharmacyGUI on the Swing Event Dispatch Thread (EDT)
 *      as required by Swing's single-thread model.
 *
 * The program ends automatically when the user clicks the window's X button
 * because PharmacyGUI sets EXIT_ON_CLOSE as its default close operation.
 */
public class Start {

    /**
     * Main method — the JVM entry point.
     *
     * @param args Command-line arguments (not used by this application).
     */
    public static void main(String[] args) {

        // --- Step 1: Ensure the data file is present before the GUI loads ---
        try {
            MedicineFileIO.createFileIfNotExists(); // Create medicines.txt if it does not exist
        } catch (IOException e) {
            // If the file cannot be created, alert the user and abort startup
            System.err.println("FATAL: Could not create the database file.");
            System.err.println("Reason: " + e.getMessage());
            System.exit(1); // Non-zero exit code signals an error to the OS
        }

        // --- Step 2: Launch the GUI on the Swing Event Dispatch Thread ---
        // All Swing components must be created and manipulated on the EDT.
        // SwingUtilities.invokeLater() schedules the Runnable to run on the EDT.
        javax.swing.SwingUtilities.invokeLater(() -> {
            new PharmacyGUI(); // Construct and display the main application window
        });
    }
}
