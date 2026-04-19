package com.mycompany.smart_home.dao.impl;

import com.mycompany.smart_home.dao.AlertLogDAO;
import com.mycompany.smart_home.models.AlertLog;
import com.mycompany.smart_home.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertLogDAOImpl implements AlertLogDAO {
    private static final Logger logger = LoggerFactory.getLogger(AlertLogDAOImpl.class);

    @Override
    public boolean addAlert(AlertLog alert) {
        String sql = "INSERT INTO alert_logs (alert_type, message, threshold, actual_value) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, alert.getAlertType());
            pstmt.setString(2, alert.getMessage());
            pstmt.setFloat(3, alert.getThreshold());
            pstmt.setFloat(4, alert.getActualValue());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm cảnh báo vào CSDL: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public List<AlertLog> getAllAlerts() {
        List<AlertLog> list = new ArrayList<>();
        String sql = "SELECT * FROM alert_logs ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                AlertLog alert = new AlertLog();
                alert.setAlertId(rs.getInt("alert_id"));
                alert.setAlertType(rs.getString("alert_type"));
                alert.setMessage(rs.getString("message"));
                alert.setThreshold(rs.getFloat("threshold"));
                alert.setActualValue(rs.getFloat("actual_value"));
                alert.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách cảnh báo: {}", e.getMessage());
        }
        return list;
    }
}