package weightcalc.db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import weightcalc.model.*;
import weightcalc.util.*;

public class DatabaseHandler {
    private Connection connection;
    private static DatabaseHandler instance;

    private DatabaseHandler() {
        initializeDatabase();
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(Constants.DB_URL);
            createTables();
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create UserProfile table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user_profiles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    height REAL NOT NULL,
                    current_weight REAL NOT NULL,
                    target_weight REAL NOT NULL,
                    age INTEGER NOT NULL,
                    gender TEXT NOT NULL,
                    activity_level TEXT NOT NULL
                )
            """);

            // Create WeightLog table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS weight_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    weight REAL NOT NULL,
                    calories_consumed INTEGER,
                    calories_burned INTEGER,
                    exercise_minutes INTEGER,
                    notes TEXT,
                    FOREIGN KEY (user_id) REFERENCES user_profiles(id)
                )
            """);
        }
    }

    public void saveUserProfile(UserProfile profile) throws SQLException {
        String sql = """
            INSERT INTO user_profiles (height, current_weight, target_weight, 
                age, gender, activity_level)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, 
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDouble(1, profile.getHeight());
            pstmt.setDouble(2, profile.getCurrentWeight());
            pstmt.setDouble(3, profile.getTargetWeight());
            pstmt.setInt(4, profile.getAge());
            pstmt.setString(5, profile.getGender());
            pstmt.setString(6, profile.getActivityLevel());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    profile.setId(rs.getInt(1));
                }
            }
        }
    }

    public void updateUserProfile(UserProfile profile) throws SQLException {
        String sql = """
            UPDATE user_profiles 
            SET height = ?, current_weight = ?, target_weight = ?, 
                age = ?, gender = ?, activity_level = ?
            WHERE id = ?
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, profile.getHeight());
            pstmt.setDouble(2, profile.getCurrentWeight());
            pstmt.setDouble(3, profile.getTargetWeight());
            pstmt.setInt(4, profile.getAge());
            pstmt.setString(5, profile.getGender());
            pstmt.setString(6, profile.getActivityLevel());
            pstmt.setInt(7, profile.getId());
            
            pstmt.executeUpdate();
        }
    }

    public UserProfile getUserProfile(int id) throws SQLException {
        String sql = "SELECT * FROM user_profiles WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserProfile profile = new UserProfile(
                        rs.getDouble("height"),
                        rs.getDouble("current_weight"),
                        rs.getDouble("target_weight"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("activity_level")
                    );
                    profile.setId(rs.getInt("id"));
                    return profile;
                }
            }
        }
        return null;
    }

    public void saveWeightLog(WeightLog log) throws SQLException {
        String sql = """
            INSERT INTO weight_logs (user_id, date, weight, calories_consumed,
                calories_burned, exercise_minutes, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, log.getUserId());
            pstmt.setString(2, DateUtils.formatForDB(log.getDate()));
            pstmt.setDouble(3, log.getWeight());
            pstmt.setInt(4, log.getCaloriesConsumed());
            pstmt.setInt(5, log.getCaloriesBurned());
            pstmt.setInt(6, log.getExerciseMinutes());
            pstmt.setString(7, log.getNotes());
            
            pstmt.executeUpdate();
        }
    }

    public List<WeightLog> getWeightLogs(int userId, LocalDate startDate, 
            LocalDate endDate) throws SQLException {
        List<WeightLog> logs = new ArrayList<>();
        String sql = """
            SELECT * FROM weight_logs 
            WHERE user_id = ? AND date BETWEEN ? AND ?
            ORDER BY date ASC
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, DateUtils.formatForDB(startDate));
            pstmt.setString(3, DateUtils.formatForDB(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WeightLog log = new WeightLog(
                        rs.getInt("user_id"),
                        DateUtils.parseDBDate(rs.getString("date")),
                        rs.getDouble("weight"),
                        rs.getInt("calories_consumed"),
                        rs.getInt("calories_burned"),
                        rs.getInt("exercise_minutes")
                    );
                    log.setId(rs.getInt("id"));
                    log.setNotes(rs.getString("notes"));
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    public WeightLog getLatestWeightLog(int userId) throws SQLException {
        String sql = "SELECT * FROM weight_logs WHERE user_id = ? ORDER BY date DESC LIMIT 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    WeightLog log = new WeightLog(
                        rs.getInt("user_id"),
                        DateUtils.parseDBDate(rs.getString("date")),
                        rs.getDouble("weight"),
                        rs.getInt("calories_consumed"),
                        rs.getInt("calories_burned"),
                        rs.getInt("exercise_minutes")
                    );
                    log.setId(rs.getInt("id"));
                    log.setNotes(rs.getString("notes"));
                    return log;
                }
            }
        }
        return null;
    }
}