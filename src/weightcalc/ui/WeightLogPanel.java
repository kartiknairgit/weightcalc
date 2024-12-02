package weightcalc.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import weightcalc.model.*;
import weightcalc.db.DatabaseHandler;

public class WeightLogPanel extends JPanel {
    private UserProfile currentUser;
    private JTextField weightField;
    private JTextField caloriesConsumedField;
    private JTextField caloriesBurnedField;
    private JSpinner exerciseMinutesSpinner;
    private JTextArea noteArea;
    private JButton logButton;
    private JTable logsTable;
    private DefaultTableModel tableModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public WeightLogPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Weight Log"));
        initializeComponents();
    }

    private void initializeComponents() {
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Weight input
        addLabel("Weight (kg):", inputPanel, gbc, 0, 0);
        weightField = new JTextField(8);
        addComponent(weightField, inputPanel, gbc, 1, 0);

        // Calories consumed
        addLabel("Calories Consumed:", inputPanel, gbc, 0, 1);
        caloriesConsumedField = new JTextField(8);
        addComponent(caloriesConsumedField, inputPanel, gbc, 1, 1);

        // Calories burned
        addLabel("Calories Burned:", inputPanel, gbc, 0, 2);
        caloriesBurnedField = new JTextField(8);
        addComponent(caloriesBurnedField, inputPanel, gbc, 1, 2);

        // Exercise minutes
        addLabel("Exercise (minutes):", inputPanel, gbc, 0, 3);
        exerciseMinutesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1440, 5));
        addComponent(exerciseMinutesSpinner, inputPanel, gbc, 1, 3);

        // Notes
        addLabel("Notes:", inputPanel, gbc, 0, 4);
        noteArea = new JTextArea(3, 20);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane noteScroll = new JScrollPane(noteArea);
        gbc.gridwidth = 2;
        addComponent(noteScroll, inputPanel, gbc, 1, 4);

        // Log button
        logButton = new JButton("Log Entry");
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(logButton, gbc);

        // Table for displaying logs
        setupLogsTable();

        // Panel layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        
        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(logsTable), BorderLayout.CENTER);

        // Add listeners
        logButton.addActionListener(e -> logEntry());
    }

    private void setupLogsTable() {
        String[] columnNames = {"Date", "Weight (kg)", "Calories In", "Calories Out", "Exercise (min)", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return LocalDate.class;
                    case 1: return Double.class;
                    case 2: case 3: case 4: return Integer.class;
                    default: return String.class;
                }
            }
        };
        
        logsTable = new JTable(tableModel);
        logsTable.setFillsViewportHeight(true);
        logsTable.setRowHeight(25);
        
        // Custom cell renderers
        TableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof LocalDate) {
                    value = ((LocalDate) value).format(dateFormatter);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        logsTable.getColumnModel().getColumn(0).setCellRenderer(dateRenderer);
        
        // Set column widths
        int[] columnWidths = {100, 80, 80, 80, 80, 200};
        for (int i = 0; i < columnWidths.length; i++) {
            logsTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    private void addLabel(String text, JPanel panel, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(new JLabel(text), gbc);
    }

    private void addComponent(JComponent component, JPanel panel, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    public void setUserProfile(Object profile) {
        if (profile instanceof UserProfile) {
            this.currentUser = (UserProfile) profile;
            loadLogs();
        }
    }

    private void logEntry() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please create a user profile first");
            return;
        }

        try {
            double weight = Double.parseDouble(weightField.getText());
            int caloriesConsumed = Integer.parseInt(caloriesConsumedField.getText());
            int caloriesBurned = Integer.parseInt(caloriesBurnedField.getText());
            int exerciseMinutes = (Integer) exerciseMinutesSpinner.getValue();

            WeightLog log = new WeightLog(
                currentUser.getId(),
                LocalDate.now(),
                weight,
                caloriesConsumed,
                caloriesBurned,
                exerciseMinutes
            );
            log.setNotes(noteArea.getText());

            DatabaseHandler.getInstance().saveWeightLog(log);
            loadLogs();
            clearInputs();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving log: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLogs() {
        if (currentUser == null) return;

        try {
            tableModel.setRowCount(0);
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(1);
            
            for (WeightLog log : DatabaseHandler.getInstance().getWeightLogs(currentUser.getId(), 
                    startDate, endDate)) {
                tableModel.addRow(new Object[]{
                    log.getDate(),
                    log.getWeight(),
                    log.getCaloriesConsumed(),
                    log.getCaloriesBurned(),
                    log.getExerciseMinutes(),
                    log.getNotes()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading logs: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputs() {
        weightField.setText("");
        caloriesConsumedField.setText("");
        caloriesBurnedField.setText("");
        exerciseMinutesSpinner.setValue(0);
        noteArea.setText("");
        weightField.requestFocus();
    }
}