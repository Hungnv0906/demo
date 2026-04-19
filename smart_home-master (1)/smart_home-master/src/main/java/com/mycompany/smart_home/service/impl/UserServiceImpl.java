package com.mycompany.smart_home.service.impl;

import com.mycompany.smart_home.dao.UserDAO;
import com.mycompany.smart_home.dao.impl.UserDAOImpl;
import com.mycompany.smart_home.models.User;
import com.mycompany.smart_home.service.UserService;

import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService{
    private UserDAO userDAO = new UserDAOImpl();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User login(String username, String rawPassword){
        User user = userDAO.findByUsername(username);
        
        if (user != null && user.getPasswordHash() != null) {
            if (BCrypt.checkpw(rawPassword, user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User loginByRfid(String rfidTag) {
        return userDAO.findByRfidTag(rfidTag);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public boolean registerUser(User user) {
        if (userDAO.findByUsername(user.getUsername()) != null) {
            logger.error("Username đã được sử dụng!");
            return false;
        }
        
        if (user.getRfidTag() != null && !user.getRfidTag().isEmpty()) {
            if (userDAO.findByRfidTag(user.getRfidTag()) != null) {
                logger.error("Thẻ RFID này đã được đăng ký cho người khác!");
                return false;
            }
        }

        String raw = user.getRawPassword(); 
        if (raw == null || raw.isBlank()) {
            logger.error("Mật khẩu không hợp lệ!");
            return false;
        }
    
        user.setPasswordHash(BCrypt.hashpw(raw, BCrypt.gensalt(12)));
    
        return userDAO.addUser(user);
    }

    @Override
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}