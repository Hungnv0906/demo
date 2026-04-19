package com.mycompany.smart_home.service.mqtt;

import com.mycompany.smart_home.dao.DeviceDAO;
import com.mycompany.smart_home.dao.impl.DeviceDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceStatusMessageHandler implements MqttMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeviceStatusMessageHandler.class);

    private DeviceDAO deviceDAO = new DeviceDAOImpl();

    @Override
    public String getTopic() {
        return "smarthome/device/+/status"; // Topic chứa wildcard
    }

    @Override
    public boolean canHandle(String topic) {
        return topic != null && topic.matches("smarthome/device/[^/]+/status");
    }

    @Override
    public void handleMessage(String topic, String payload) {
        try {
            deviceDAO.updateStatusByTopic(topic, payload);
            logger.debug("Đã cập nhật trạng thái thiết bị qua topic {}: {}", topic, payload);
        } catch (Exception e) {
            logger.error("Lỗi khi update database cho topic {}: {}", topic, e.getMessage());
            throw e;
        }
    }
}