package com.mycompany.smart_home.dao.impl;

import com.mycompany.smart_home.dao.EnvironmentLogDAO;
import com.mycompany.smart_home.models.EnvironmentLog;
import com.mycompany.smart_home.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentLogDAOImpl implements EnvironmentLogDAO {
    //tạo trình ghi nhật ký (Logger) để ghi lại các lỗi xảy ra trong lớp này
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentLogDAOImpl.class);
    //chuyển dữ liệu từ bảng SQL sang đối tượng Java
    private EnvironmentLog mapResultSetToLog(ResultSet rs) throws SQLException {
        EnvironmentLog log = new EnvironmentLog();
        log.setLogId(rs.getInt("log_id"));
        log.setTemperature(rs.getFloat("temperature"));
        log.setHumidity(rs.getFloat("humidity"));
        log.setRecordedAt(rs.getTimestamp("recorded_at"));
        
        return log;
    }

    @Override
    public boolean addLog(EnvironmentLog log) {
        String sql = "INSERT INTO environment_logs (temperature, humidity, recorded_at) VALUES (?, ?, NOW())";
        
        //dùng try-with-resources tự đóng kết nối sau khi thực thi
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            //điền gtri nhiệt độ, độ ẩm, thời gian vào ???.
            ps.setFloat(1, log.getTemperature());
            ps.setFloat(2, log.getHumidity());
            //thực thi nếu số dòng bị tác động >0 => True
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm log: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public List<EnvironmentLog> getLatestLogs(int limit) {
        //dsach rỗng để chứa kq
        List<EnvironmentLog> list = new ArrayList<>();
        String sql = "SELECT * FROM environment_logs ORDER BY recorded_at DESC LIMIT ?";
        //dùng try-with-resources tự đóng kết nối sau khi thực thi
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //điền số lượng giới hạn vào dấu hỏi
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                // duyệt qua ResultSet và sử dụng hàm mapResultSetToLog để chuyển đổi sang Object
                while (rs.next()) {
                    list.add(mapResultSetToLog(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách log: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public boolean deleteOldLogs(int daysLimit) {
        // dùng hàm NOW() và INTERVAL của SQL đế xác định mốc thời gian cần xóa
        String sql = "DELETE FROM environment_logs WHERE recorded_at < (NOW() - INTERVAL ? DAY)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, daysLimit);
            int rows = ps.executeUpdate();
            // trả về true nếu quá trình xóa diễn ra bình thường
            return rows >= 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa log cũ: {}", e.getMessage());
        }
        return false;
    }
}
