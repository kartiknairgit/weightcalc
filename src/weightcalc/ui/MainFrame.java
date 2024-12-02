package weightcalc.ui;

import javax.swing.*;
import java.awt.*;
import weightcalc.model.UserProfile;

public class MainFrame extends JFrame {
    private UserProfilePanel userProfilePanel;
    private WeightLogPanel weightLogPanel;
    private CalorieCalculatorPanel calorieCalculatorPanel;

    public MainFrame() {
        setTitle("Weight Loss Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));  // Added minimum size
        
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        userProfilePanel = new UserProfilePanel();
        weightLogPanel = new WeightLogPanel();
        calorieCalculatorPanel = new CalorieCalculatorPanel();

        // Connect panels through events
        userProfilePanel.addPropertyChangeListener("userProfileSaved", evt -> {
            if (evt.getNewValue() instanceof UserProfile) {
                weightLogPanel.setUserProfile(evt.getNewValue());
                calorieCalculatorPanel.updateFromProfile(evt.getNewValue());
            }
        });
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Left panel for user profile and calorie calculator
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(350, 0));  // Fixed width for left panel
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        leftPanel.add(userProfilePanel, BorderLayout.NORTH);
        leftPanel.add(calorieCalculatorPanel, BorderLayout.CENTER);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.add(weightLogPanel, BorderLayout.CENTER);

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Add padding to main container
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}