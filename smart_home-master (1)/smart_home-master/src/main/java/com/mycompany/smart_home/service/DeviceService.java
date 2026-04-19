package com.mycompany.smart_home.service;

import com.mycompany.smart_home.models.Device;

import java.util.List;

public interface DeviceService{
    List<Device> getAllDevices();
    boolean addDevice(Device device);
    boolean deleteDevice(int deviceId);
    boolean changeDeviceStatus(int deviceId, String newStatus);
    boolean syncStatusFromMqtt(String topic, String newStatus);
}