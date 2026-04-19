package com.mycompany.smart_home.dao;

import com.mycompany.smart_home.models.Device;
import java.util.List;

public interface DeviceDAO {
    List<Device> getAllDevices();
    boolean addDevice(Device device);
    boolean updateDeviceStatus(int deviceId, String newStatus);
    boolean updateStatusByTopic(String topic, String newStatus);
    boolean deleteDevice(int deviceId);
}