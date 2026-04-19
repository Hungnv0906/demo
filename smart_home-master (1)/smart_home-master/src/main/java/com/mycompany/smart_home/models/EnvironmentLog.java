package com.mycompany.smart_home.models;

import java.sql.Timestamp;

public class EnvironmentLog{
    private int logId;
    private float temperature;
    private float humidity;
    private Timestamp recordedAt;

    public EnvironmentLog(){}

    public EnvironmentLog(int logId, float temperature, float humidity, Timestamp recordedAt){
        this.logId = logId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.recordedAt = recordedAt;
    }

    public int getLogId(){
        return logId;
    }

    public void setLogId(int logId){
        this.logId = logId;
    }

    public float getTemperature(){
        return temperature;
    }

    public void setTemperature(float temperature){
        this.temperature = temperature;
    }

    public float getHumidity(){
        return humidity;
    }

    public void setHumidity(float humidity){
        this.humidity = humidity;
    }

    public Timestamp getRecordedAt(){
        return recordedAt;
    }
    
    public void setRecordedAt(Timestamp recordedAt){
        this.recordedAt = recordedAt;
    }
}
