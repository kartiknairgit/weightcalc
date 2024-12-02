package weightcalc;

import javax.swing.SwingUtilities;
import weightcalc.ui.MainFrame;

/**
 * Main entry point for the Weight Loss Tracker application.
 * This class initializes the GUI and starts the application.
 */
public class WeightLossTracker {
    
    /**
     * Application version number
     */
    public static final String VERSION = "1.0.0";
    
    /**
     * Main method to start the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the look and feel to the system default
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
                
                // Create and display the main application window
                MainFrame mainFrame = new MainFrame();
                mainFrame.setTitle("Weight Loss Tracker v" + VERSION);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null); // Center on screen
                mainFrame.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    /**
     * Returns the application version
     * @return String containing version number
     */
    public static String getVersion() {
        return VERSION;
    }
}