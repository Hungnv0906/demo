package com.mycompany.smart_home.models;

import java.sql.Timestamp;

public class AlertLog {
    private int alertId;
    private String alertType;
    private String message;
    private float threshold;
    private float actualValue;
    private Timestamp createdAt;

    public AlertLog() {}

    public int getAlertId() { return alertId; }
    public void setAlertId(int alertId) { this.alertId = alertId; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public float getThreshold() { return threshold; }
    public void setThreshold(float threshold) { this.threshold = threshold; }
    public float getActualValue() { return actualValue; }
    public void setActualValue(float actualValue) { this.actualValue = actualValue; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}