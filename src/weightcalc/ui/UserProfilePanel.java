package weightcalc.ui;

import javax.swing.*;
import java.awt.*;
import weightcalc.model.UserProfile;
import weightcalc.db.DatabaseHandler;
import weightcalc.util.Constants;

public class UserProfilePanel extends JPanel {
   private JTextField heightField;
   private JTextField weightField;
   private JTextField targetWeightField;
   private JTextField ageField;
   private JComboBox<String> genderCombo;
   private JComboBox<String> activityLevelCombo;
   private JButton saveButton;
   private UserProfile currentProfile;

   public UserProfilePanel() {
       setLayout(new GridBagLayout());
       setBorder(BorderFactory.createTitledBorder("User Profile"));
       initializeComponents();
   }

   private void initializeComponents() {
       GridBagConstraints gbc = new GridBagConstraints();
       gbc.insets = new Insets(5, 5, 5, 5);
       gbc.anchor = GridBagConstraints.WEST;

       // Height
       addLabel("Height (cm):", gbc, 0, 0);
       heightField = new JTextField(10);
       addComponent(heightField, gbc, 1, 0);

       // Current Weight 
       addLabel("Current Weight (kg):", gbc, 0, 1);
       weightField = new JTextField(10);
       addComponent(weightField, gbc, 1, 1);

       // Target Weight
       addLabel("Target Weight (kg):", gbc, 0, 2);
       targetWeightField = new JTextField(10);
       addComponent(targetWeightField, gbc, 1, 2);

       // Age
       addLabel("Age:", gbc, 2, 0);
       ageField = new JTextField(10);
       addComponent(ageField, gbc, 3, 0);

       // Gender
       addLabel("Gender:", gbc, 2, 1);
       genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
       addComponent(genderCombo, gbc, 3, 1);

       // Activity Level
       addLabel("Activity Level:", gbc, 2, 2);
       activityLevelCombo = new JComboBox<>(new String[]{
           "Sedentary",
           "Light Activity", 
           "Moderate Activity",
           "Very Active"
       });
       addComponent(activityLevelCombo, gbc, 3, 2);

       // Save Button
       saveButton = new JButton("Save Profile");
       gbc.gridx = 3;
       gbc.gridy = 3;
       gbc.anchor = GridBagConstraints.CENTER;
       add(saveButton, gbc);

       saveButton.addActionListener(e -> saveProfile());
   }

   private void addLabel(String text, GridBagConstraints gbc, int x, int y) {
       gbc.gridx = x;
       gbc.gridy = y;
       add(new JLabel(text), gbc);
   }

   private void addComponent(JComponent component, GridBagConstraints gbc, int x, int y) {
       gbc.gridx = x;
       gbc.gridy = y;
       add(component, gbc);
   }

   private boolean validateInputs() {
       try {
           double height = Double.parseDouble(heightField.getText());
           double weight = Double.parseDouble(weightField.getText());
           double targetWeight = Double.parseDouble(targetWeightField.getText());
           int age = Integer.parseInt(ageField.getText());

           if (height < Constants.MIN_HEIGHT_CM || height > Constants.MAX_HEIGHT_CM) {
               showError("Height must be between " + Constants.MIN_HEIGHT_CM + 
                        " and " + Constants.MAX_HEIGHT_CM + " cm");
               return false;
           }
           if (weight < Constants.MIN_WEIGHT_KG || weight > Constants.MAX_WEIGHT_KG) {
               showError("Weight must be between " + Constants.MIN_WEIGHT_KG + 
                        " and " + Constants.MAX_WEIGHT_KG + " kg");
               return false;
           }
           if (targetWeight < Constants.MIN_WEIGHT_KG || targetWeight > Constants.MAX_WEIGHT_KG) {
               showError("Target weight must be between " + Constants.MIN_WEIGHT_KG + 
                        " and " + Constants.MAX_WEIGHT_KG + " kg");
               return false;
           }
           if (age < 18 || age > 100) {
               showError("Age must be between 18 and 100");
               return false;
           }
           return true;
       } catch (NumberFormatException e) {
           showError("Please enter valid numbers for height, weight, and age");
           return false;
       }
   }

   private void saveProfile() {
       if (!validateInputs()) {
           return;
       }

       try {
           UserProfile profile = new UserProfile(
               Double.parseDouble(heightField.getText()),
               Double.parseDouble(weightField.getText()),
               Double.parseDouble(targetWeightField.getText()),
               Integer.parseInt(ageField.getText()),
               (String) genderCombo.getSelectedItem(),
               (String) activityLevelCombo.getSelectedItem()
           );

           if (currentProfile != null && currentProfile.getId() > 0) {
               profile.setId(currentProfile.getId());
               DatabaseHandler.getInstance().updateUserProfile(profile);
           } else {
               DatabaseHandler.getInstance().saveUserProfile(profile);
           }

           currentProfile = profile;
           showMessage("Profile saved successfully!");
           
           // Notify other panels
           firePropertyChange("userProfileSaved", null, profile);

       } catch (Exception e) {
           showError("Error saving profile: " + e.getMessage());
       }
   }

   public void loadProfile(UserProfile profile) {
       if (profile != null) {
           currentProfile = profile;
           heightField.setText(String.valueOf(profile.getHeight()));
           weightField.setText(String.valueOf(profile.getCurrentWeight()));
           targetWeightField.setText(String.valueOf(profile.getTargetWeight()));
           ageField.setText(String.valueOf(profile.getAge()));
           genderCombo.setSelectedItem(profile.getGender());
           activityLevelCombo.setSelectedItem(profile.getActivityLevel());
       }
   }

   private void showError(String message) {
       JOptionPane.showMessageDialog(this, message, "Input Error", 
           JOptionPane.ERROR_MESSAGE);
   }

   private void showMessage(String message) {
       JOptionPane.showMessageDialog(this, message, "Success", 
           JOptionPane.INFORMATION_MESSAGE);
   }
}