package com.mycompany.smart_home.models;

import java.sql.Timestamp;

public class AccessLog{
    private int accessId;
    private String rfidTag;
    private Integer userId;
    private String accessStatus;
    private Timestamp accessedAt;

    public AccessLog(){}
    
    public AccessLog(int accessId, String rfidTag, Integer userId, String accessStatus, Timestamp accessedAt){
        this.accessId = accessId;
        this.rfidTag = rfidTag;
        this.userId = userId;
        this.accessStatus = accessStatus;
        this.accessedAt = accessedAt;
    }

    public int getAccessId(){
        return accessId;
    }

    public void setAccessId(int accessId){ 
        this.accessId = accessId;
    }

    public String getRfidTag(){ 
        return rfidTag;
    }

    public void setRfidTag(String rfidTag){
        this.rfidTag = rfidTag;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public String getAccessStatus(){
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus){
        this.accessStatus = accessStatus;
    }

    public Timestamp getAccessedAt(){
        return accessedAt;
    }

    public void setAccessedAt(Timestamp accessedAt){
        this.accessedAt = accessedAt;
    }
}
