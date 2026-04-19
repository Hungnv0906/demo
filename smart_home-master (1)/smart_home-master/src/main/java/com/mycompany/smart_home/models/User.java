package com.mycompany.smart_home.models;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String rawPassword;
    private String rfidTag;
    private String fullName;
    private String userRole;
    
    public User(){}
    
    public User(int userId, String username, String passwordHash, String rfidTag, String fullName, String userRole){
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rfidTag = rfidTag;
        this.fullName = fullName;
        this.userRole = userRole;
    }
    
    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getPasswordHash(){
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
    
    public String getRawPassword(){
        return rawPassword;
    }
    public void setRawPassword(String rawPassword){
        this.rawPassword = rawPassword;
    }
    
    public String getRfidTag(){
        return rfidTag;
    }

    public void setRfidTag(String rfidTag){
        this.rfidTag = rfidTag;
    }
    
    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    
    public String getUserRole(){
        return userRole;
    }

    public void setUserRole(String userRole){
        this.userRole = userRole;
    }
}