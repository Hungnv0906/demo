package com.mycompany.smart_home.dao.impl;

import com.mycompany.smart_home.dao.DeviceDAO;
import com.mycompany.smart_home.models.Device;
import com.mycompany.smart_home.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceDAOImpl implements DeviceDAO{
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceDAOImpl.class);

    @Override
    public List<Device> getAllDevices(){
        List<Device> deviceList = new ArrayList<>();
        String sql = "SELECT * FROM devices";

        // dùng try-with-resources 
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            // duyệt qua từng bản ghi trả về từ bảng devices
            while(rs.next()){
                Device device = new Device();

                // mapping dữ liệu từ các cột của DB sang các thuộc tính của đối tượng Device
                device.setDeviceId(rs.getInt("device_id"));
                device.setDeviceName(rs.getString("device_name"));
                device.setDeviceType(rs.getString("device_type"));
                device.setMqttTopic(rs.getString("mqtt_topic"));
                device.setCurrentStatus(rs.getString("current_status"));
                device.setLastUpdated(rs.getTimestamp("last_updated"));

                // thêm thiết bị vào danh sách trả về
                deviceList.add(device);
            }
        }catch(SQLException e){
            logger.error("Lỗi khi lấy danh sách thiết bị: {}", e.getMessage());
        }
        return deviceList;
    }

    @Override
    public boolean updateDeviceStatus(int deviceId, String newStatus){
        String sql = "UPDATE devices SET current_status = ? WHERE device_id = ?";
        
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            // truyền tham số vào câu lệnh SQL, tránh SQL Injection
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, deviceId);
            
            // executeUpdate trả về số dòng bị ảnh hưởng, nếu >0 là thành công
            return pstmt.executeUpdate() > 0;
            
        }catch (SQLException e){
            logger.error("Lỗi khi cập nhật trạng thái thiết bị: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addDevice(Device device){
        String sql = "INSERT INTO devices (device_name, device_type, mqtt_topic, current_status) VALUES (?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            // gán các thông tin từ đối tượng Java vào câu lệnh INSERT
            pstmt.setString(1, device.getDeviceName());
            pstmt.setString(2, device.getDeviceType());
            pstmt.setString(3, device.getMqttTopic());
            pstmt.setString(4, device.getCurrentStatus());
            
            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            logger.error("Lỗi khi thêm thiết bị mới: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteDevice(int deviceId){
        String sql = "DELETE FROM devices WHERE device_id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setInt(1, deviceId);
            
            return pstmt.executeUpdate() > 0;
            
        }catch(SQLException e){
            logger.error("Lỗi khi xóa thiết bị: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateStatusByTopic(String topic, String newStatus){
        String sql = "UPDATE devices SET current_status = ? WHERE mqtt_topic = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, topic);
            
            return pstmt.executeUpdate() > 0;
            
        }catch (SQLException e){
            logger.error("Lỗi khi cập nhật trạng thái qua MQTT Topic: {}", e.getMessage());
        }
        return false;
    }
}
