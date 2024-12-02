package weightcalc.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date-related operations
 */
public class DateUtils {
    private static final DateTimeFormatter DISPLAY_FORMATTER = 
        DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DISPLAY);
    private static final DateTimeFormatter DB_FORMATTER = 
        DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DB);
    
    /**
     * Converts a LocalDate to display format
     */
    public static String formatForDisplay(LocalDate date) {
        return date != null ? date.format(DISPLAY_FORMATTER) : "";
    }
    
    /**
     * Converts a LocalDate to database format
     */
    public static String formatForDB(LocalDate date) {
        return date != null ? date.format(DB_FORMATTER) : "";
    }
    
    /**
     * Parses a display-formatted date string to LocalDate
     */
    public static LocalDate parseDisplayDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DISPLAY_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Parses a database-formatted date string to LocalDate
     */
    public static LocalDate parseDBDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DB_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Calculates the number of weeks between two dates
     */
    public static long weeksBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.WEEKS.between(startDate, endDate);
    }
    
    /**
     * Checks if a date is within a valid range (not in future, not too far in past)
     */
    public static boolean isDateValid(LocalDate date) {
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusYears(5); // 5 years in the past
        return !date.isAfter(now) && !date.isBefore(minDate);
    }
    
    /**
     * Gets the start of the week for a given date
     */
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue() - 1);
    }
    
    /**
     * Gets the end of the week for a given date
     */
    public static LocalDate getEndOfWeek(LocalDate date) {
        return getStartOfWeek(date).plusDays(6);
    }
    
    private DateUtils() {
        // Private constructor to prevent instantiation
    }
}