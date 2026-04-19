package com.mycompany.smart_home.dao.impl;

import com.mycompany.smart_home.dao.UserDAO;
import com.mycompany.smart_home.models.User;
import com.mycompany.smart_home.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAOImpl implements UserDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash")); 
        user.setRfidTag(rs.getString("rfid_tag"));           
        user.setFullName(rs.getString("full_name"));
        user.setUserRole(rs.getString("user_role"));         
        
        return user;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm user: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public User findByRfidTag(String rfidTag){
        if (rfidTag == null || rfidTag.isBlank()) {
            return null;
        }

        String sql = "SELECT * FROM users WHERE rfid_tag = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, rfidTag);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToUser(rs);
                }
            }

        }catch(SQLException e){
            logger.error("Lỗi khi tìm người dùng bằng rfid tag: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                User user = mapResultSetToUser(rs);
                userList.add(user);
            }

        }catch(SQLException e){
            logger.error("Lỗi khi lấy toàn bộ người dùng: {}", e.getMessage());
        }
        return userList;
    }

    @Override
    public boolean addUser(User user){
        String sql = "INSERT INTO users (username, password_hash, rfid_tag, full_name, user_role) VALUES(?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getRfidTag());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getUserRole());

            return pstmt.executeUpdate() > 0;
        }catch(SQLException e){
            logger.error("Lỗi khi thêm người dùng: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateUser(User user){
        String sql = "UPDATE users SET username = ?, password_hash = ?, full_name = ?, user_role = ?, rfid_tag = ? WHERE user_id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getUserRole());
            pstmt.setString(5, user.getRfidTag());
            pstmt.setInt(6, user.getUserId());

            return pstmt.executeUpdate() > 0;

        } catch(SQLException e){
            logger.error("Lỗi khi cập nhật người dùng: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteUser(int userId){
        String sql = "DELETE FROM users WHERE user_id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setInt(1, userId);

            return pstmt.executeUpdate() > 0;
        }catch(SQLException e){
            logger.error("Lỗi khi xóa người dùng: {}", e.getMessage());
        }
        return false;
    }
}
