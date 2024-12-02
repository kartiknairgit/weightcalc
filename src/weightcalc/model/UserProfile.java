package weightcalc.model;

import java.time.LocalDate;

public class UserProfile {
    private int id;
    private double height;
    private double currentWeight;
    private double targetWeight;
    private int age;
    private String gender;
    private String activityLevel;
    private LocalDate startDate;
    private LocalDate targetDate;

    public UserProfile(double height, double currentWeight, double targetWeight, 
                      int age, String gender, String activityLevel) {
        this.height = height;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
        this.startDate = LocalDate.now();
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    public double getCurrentWeight() { return currentWeight; }
    public void setCurrentWeight(double weight) { this.currentWeight = weight; }
    
    public double getTargetWeight() { return targetWeight; }
    public void setTargetWeight(double weight) { this.targetWeight = weight; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String level) { this.activityLevel = level; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate date) { this.startDate = date; }
    
    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate date) { this.targetDate = date; }
}