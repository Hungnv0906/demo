package com.mycompany.smart_home.dao;

import com.mycompany.smart_home.models.User;
import java.util.List;

public interface UserDAO {
    User findByUsername(String username);
    User findByRfidTag(String rfidTag);
    List<User> getAllUsers();
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}