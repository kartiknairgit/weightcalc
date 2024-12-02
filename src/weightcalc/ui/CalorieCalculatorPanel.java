package weightcalc.ui;

import javax.swing.*;
import java.awt.*;
import weightcalc.model.UserProfile;
import weightcalc.util.Constants;

public class CalorieCalculatorPanel extends JPanel {
    private JTextField bmrField;
    private JTextField tdeeField;
    private JTextField dailyCaloriesField;
    private JComboBox<String> activityLevelCombo;
    private UserProfile currentProfile;  // Added this field

    public CalorieCalculatorPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Calorie Calculator"));
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // BMR
        addLabel("Basal Metabolic Rate:", mainPanel, gbc, 0, 0);
        bmrField = new JTextField(10);
        bmrField.setEditable(false);
        addComponent(bmrField, mainPanel, gbc, 1, 0);

        // Activity Level
        addLabel("Activity Level:", mainPanel, gbc, 0, 1);
        activityLevelCombo = new JComboBox<>(new String[]{
            "Sedentary", "Light Activity", "Moderate Activity", "Very Active"
        });
        addComponent(activityLevelCombo, mainPanel, gbc, 1, 1);

        // TDEE
        addLabel("Total Daily Energy Expenditure:", mainPanel, gbc, 0, 2);
        tdeeField = new JTextField(10);
        tdeeField.setEditable(false);
        addComponent(tdeeField, mainPanel, gbc, 1, 2);

        // Daily Calories Target
        addLabel("Daily Calorie Target:", mainPanel, gbc, 0, 3);
        dailyCaloriesField = new JTextField(10);
        dailyCaloriesField.setEditable(false);
        addComponent(dailyCaloriesField, mainPanel, gbc, 1, 3);

        add(mainPanel, BorderLayout.NORTH);

        activityLevelCombo.addActionListener(e -> updateCalculations());
        
        // Initialize fields with empty values
        clearCalculations();
    }

    private void addLabel(String text, JPanel panel, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(new JLabel(text), gbc);
    }

    private void addComponent(JComponent component, JPanel panel, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    public void updateFromProfile(Object profile) {
        if (!(profile instanceof UserProfile)) {
            clearCalculations();
            return;
        }
        
        currentProfile = (UserProfile) profile;
        activityLevelCombo.setSelectedItem(currentProfile.getActivityLevel());
        updateCalculations();
    }

    private void clearCalculations() {
        bmrField.setText("0");
        tdeeField.setText("0");
        dailyCaloriesField.setText("0");
    }

    private void updateCalculations() {
        if (currentProfile == null) {
            clearCalculations();
            return;
        }

        double bmr = calculateBMR();
        double tdee = calculateTDEE(bmr);
        double dailyTarget = calculateDailyTarget(tdee);

        bmrField.setText(String.format("%.0f", bmr));
        tdeeField.setText(String.format("%.0f", tdee));
        dailyCaloriesField.setText(String.format("%.0f", dailyTarget));
    }

    private double calculateBMR() {
        // Mifflin-St Jeor Equation
        double bmr;
        if (currentProfile.getGender().equalsIgnoreCase("male")) {
            bmr = (10 * currentProfile.getCurrentWeight()) + 
                  (6.25 * currentProfile.getHeight()) - 
                  (5 * currentProfile.getAge()) + 5;
        } else {
            bmr = (10 * currentProfile.getCurrentWeight()) + 
                  (6.25 * currentProfile.getHeight()) - 
                  (5 * currentProfile.getAge()) - 161;
        }
        return bmr;
    }

    private double calculateTDEE(double bmr) {
        String activity = (String) activityLevelCombo.getSelectedItem();
        switch (activity) {
            case "Sedentary": return bmr * Constants.SEDENTARY_MULTIPLIER;
            case "Light Activity": return bmr * Constants.LIGHT_ACTIVITY_MULTIPLIER;
            case "Moderate Activity": return bmr * Constants.MODERATE_ACTIVITY_MULTIPLIER;
            case "Very Active": return bmr * Constants.VERY_ACTIVE_MULTIPLIER;
            default: return bmr * Constants.SEDENTARY_MULTIPLIER;
        }
    }

    private double calculateDailyTarget(double tdee) {
        // Create a 500 calorie deficit for healthy weight loss
        return tdee - 500;
    }
}