package com.mycompany.smart_home.dao.impl;

import com.mycompany.smart_home.dao.AccessLogDAO;
import com.mycompany.smart_home.models.AccessLog;
import com.mycompany.smart_home.utils.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessLogDAOImpl implements AccessLogDAO{
    
    private static final Logger logger = LoggerFactory.getLogger(AccessLogDAOImpl.class);
    
    @Override
    public boolean addAccessLog(AccessLog log){
        // lệnh SQL thêm dữ liệu (thêm tag RFID, ID user và trạng thái)
        String sql = "INSERT INTO access_logs (rfid_tag, user_id, access_status) VALUES (?, ?, ?)";
        
        // dùng try-with-resources auto đóng Cnt & PS
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // tạo giá trị cho các dấu hỏi (?) trong câu lệnh SQL
            pstmt.setString(1, log.getRfidTag());
            if (log.getUserId() != null) {
                pstmt.setInt(2, log.getUserId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, log.getAccessStatus());

            // thực thi và kiểm tra xem có dòng nào trong DB bị thay đổi không
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // ghi lại lỗi nếu truy vấn gặp vấn đề
            logger.error("Lỗi khi thêm lịch sử ra vào: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public List<AccessLog> getAllAccessLogs(){
        List<AccessLog> accessLogList = new ArrayList<>();
        // lệnh SQL lấy tất cả bản ghi từ bảng access_log
        String sql = "SELECT * FROM access_logs";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            
            // duyệt qua từng dòng kết quả trả về từ database
            while(rs.next()){
                AccessLog accessLog = new AccessLog();

                // mapping dữ liệu từ SQL gán vào đối tượng java
                accessLog.setAccessId(rs.getInt("access_id"));
                accessLog.setRfidTag(rs.getString("rfid_tag"));
                accessLog.setUserId(rs.getObject("user_id", Integer.class));
                accessLog.setAccessStatus(rs.getString("access_status"));
                accessLog.setAccessedAt(rs.getTimestamp("accessed_at"));

                // thêm đối tượng đã điền dữ liệu vào danh sách trả về
                accessLogList.add(accessLog);
            }
        }catch(SQLException e){
            logger.error("Lỗi khi lấy lịch sử đăng nhập: {}", e.getMessage());
        }
        return accessLogList;
    }

    @Override
    public List<AccessLog> getLogsByUser(int userId){
        List<AccessLog> accessLogList = new ArrayList<>();
        // lệnh SQL lọc theo user_id
        String sql = "SELECT * FROM access_logs WHERE user_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            // gán giá trị userId vào tham số của lệnh SQL
            pstmt.setInt(1, userId);

            // thực thi truy vấn và nhận về tập kết quả ResultSet
            try(ResultSet rs = pstmt.executeQuery()){

                while(rs.next()){
                    AccessLog accessLog = new AccessLog();

                    // gán dữ liệu từ DB vào đối tượng
                    accessLog.setAccessId(rs.getInt("access_id"));
                    accessLog.setRfidTag(rs.getString("rfid_tag"));
                    accessLog.setUserId(rs.getObject("user_id", Integer.class));
                    accessLog.setAccessStatus(rs.getString("access_status"));
                    accessLog.setAccessedAt(rs.getTimestamp("accessed_at"));

                    accessLogList.add(accessLog);
                }
            }  
        }catch(SQLException e){
            logger.error("Lỗi khi lấy lịch sử ra vào của người dùng: {}", e.getMessage());
        }
        return accessLogList;
    }
}
