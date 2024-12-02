package weightcalc.model;

import java.time.LocalDate;

public class WeightLog {
    private int id;
    private int userId;
    private LocalDate date;
    private double weight;
    private int caloriesConsumed;
    private int caloriesBurned;
    private int exerciseMinutes;
    private String notes;

    public WeightLog(int userId, LocalDate date, double weight, 
                    int caloriesConsumed, int caloriesBurned, int exerciseMinutes) {
        this.userId = userId;
        this.date = date;
        this.weight = weight;
        this.caloriesConsumed = caloriesConsumed;
        this.caloriesBurned = caloriesBurned;
        this.exerciseMinutes = exerciseMinutes;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public int getCaloriesConsumed() { return caloriesConsumed; }
    public void setCaloriesConsumed(int calories) { this.caloriesConsumed = calories; }
    
    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int calories) { this.caloriesBurned = calories; }
    
    public int getExerciseMinutes() { return exerciseMinutes; }
    public void setExerciseMinutes(int minutes) { this.exerciseMinutes = minutes; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}