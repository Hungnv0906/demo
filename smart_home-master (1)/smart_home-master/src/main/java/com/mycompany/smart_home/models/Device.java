package com.mycompany.smart_home.models;

import java.sql.Timestamp;

public class Device{
    private int deviceId;
    private String deviceName;
    private String deviceType;
    private String mqttTopic;
    private String currentStatus;
    private Timestamp lastUpdated;

    public Device(){}

    public Device(int deviceId, String deviceName, String deviceType, String mqttTopic,
                    String currentStatus, Timestamp lastUpdated) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.mqttTopic = mqttTopic;
        this.currentStatus = currentStatus;
        this.lastUpdated = lastUpdated;
    }

    public int getDeviceId(){
        return deviceId;
    }

    public void setDeviceId(int deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceName(){
        return deviceName;
    }

    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }

    public String getDeviceType(){
        return deviceType;
    }

    public void setDeviceType(String deviceType){
        this.deviceType = deviceType;
    }

    public String getMqttTopic(){
        return mqttTopic;
    }

    public void setMqttTopic(String mqttTopic){
        this.mqttTopic = mqttTopic;
    }

    public String getCurrentStatus(){
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus){
        this.currentStatus = currentStatus;
    }

    public Timestamp getLastUpdated(){
        return lastUpdated;
    }
    
    public void setLastUpdated(Timestamp lastUpdated){
        this.lastUpdated = lastUpdated;
    }
}