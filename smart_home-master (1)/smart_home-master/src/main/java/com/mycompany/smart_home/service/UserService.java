package com.mycompany.smart_home.service;

import com.mycompany.smart_home.models.User;
import java.util.List;

public interface UserService{
    User login(String username, String rawPassword);
    User loginByRfid(String rfidTag);
    List<User> getAllUsers();
    boolean registerUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}