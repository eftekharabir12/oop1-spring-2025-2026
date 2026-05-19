package pharmacymanagementsystem;

import java.io.IOException;
import pharmacymanagementsystem.fileio.MedicineFileIO;
import pharmacymanagementsystem.gui.PharmacyGUI;


public class Start {

    
    public static void main(String[] args) {

       
        try {
            MedicineFileIO.createFileIfNotExists(); 
        } catch (IOException e) {
            
            System.err.println("FATAL: Could not create the database file.");
            System.err.println("Reason: " + e.getMessage());
            System.exit(1);
        }

        
        javax.swing.SwingUtilities.invokeLater(() -> {
            new PharmacyGUI(); 
        });
    }
}
