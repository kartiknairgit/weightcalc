package weightcalc.util;

/**
 * Application-wide constants
 */
public class Constants {
    // Database constants
    public static final String DB_NAME = "weightloss_db";
    public static final String DB_URL = "jdbc:sqlite:" + DB_NAME;
    
    // Weight-related constants
    public static final double MIN_WEIGHT_KG = 30.0;
    public static final double MAX_WEIGHT_KG = 300.0;
    public static final double MIN_HEIGHT_CM = 100.0;
    public static final double MAX_HEIGHT_CM = 250.0;
    
    // Calorie-related constants
    public static final int MIN_DAILY_CALORIES = 1200;
    public static final int MAX_DAILY_CALORIES = 5000;
    public static final double CALORIES_PER_KG = 7700.0; // Approximate calories in 1kg of fat
    
    // UI constants
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    public static final int PADDING = 10;
    public static final String DATE_FORMAT_DISPLAY = "dd-MM-yyyy";
    public static final String DATE_FORMAT_DB = "yyyy-MM-dd";
    
    // Weight loss related constants
    public static final double HEALTHY_WEIGHT_LOSS_PER_WEEK = 0.5; // kg
    public static final double MAX_WEIGHT_LOSS_PER_WEEK = 1.0; // kg
    
    // Activity level multipliers for BMR
    public static final double SEDENTARY_MULTIPLIER = 1.2;
    public static final double LIGHT_ACTIVITY_MULTIPLIER = 1.375;
    public static final double MODERATE_ACTIVITY_MULTIPLIER = 1.55;
    public static final double VERY_ACTIVE_MULTIPLIER = 1.725;
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}