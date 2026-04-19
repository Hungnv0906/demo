package com.mycompany.smart_home.service.impl;

import com.mycompany.smart_home.dao.DeviceDAO;
import com.mycompany.smart_home.dao.impl.DeviceDAOImpl;
import com.mycompany.smart_home.models.Device;
import com.mycompany.smart_home.service.DeviceService;

import java.util.List;

public class DeviceServiceImpl implements DeviceService {
    private DeviceDAO deviceDAO = new DeviceDAOImpl();

    @Override
    public List<Device> getAllDevices() {
        return deviceDAO.getAllDevices();
    }

    @Override
    public boolean addDevice(Device device) {
        if (device.getCurrentStatus() == null || device.getCurrentStatus().isEmpty()) {
            device.setCurrentStatus("OFF");
        }
        return deviceDAO.addDevice(device);
    }

    @Override
    public boolean deleteDevice(int deviceId) {
        return deviceDAO.deleteDevice(deviceId);
    }

    @Override
    public boolean changeDeviceStatus(int deviceId, String newStatus) {
        return deviceDAO.updateDeviceStatus(deviceId, newStatus);
    }

    @Override
    public boolean syncStatusFromMqtt(String topic, String newStatus) {
        return deviceDAO.updateStatusByTopic(topic, newStatus);
    }
}