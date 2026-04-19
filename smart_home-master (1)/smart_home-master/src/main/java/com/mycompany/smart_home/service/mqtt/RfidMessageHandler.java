package com.mycompany.smart_home.service.mqtt;

import com.mycompany.smart_home.dao.AccessLogDAO;
import com.mycompany.smart_home.dao.UserDAO;
import com.mycompany.smart_home.dao.impl.AccessLogDAOImpl;
import com.mycompany.smart_home.dao.impl.UserDAOImpl;
import com.mycompany.smart_home.models.AccessLog;
import com.mycompany.smart_home.models.User;
import com.mycompany.smart_home.service.MqttService;
import com.mycompany.smart_home.ui.MainDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.SwingUtilities;

public class RfidMessageHandler implements MqttMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(RfidMessageHandler.class);

    private UserDAO userDAO = new UserDAOImpl();
    private AccessLogDAO accessLogDAO = new AccessLogDAOImpl();
    private MainDashboard dashboard;
    private MqttService mqttService;

    public RfidMessageHandler(MainDashboard dashboard, MqttService mqttService) {
        this.dashboard = dashboard;
        this.mqttService = mqttService;
    }

    @Override
    public String getTopic() {
        return "smarthome/rfid/quet_the";
    }

    @Override
    public boolean canHandle(String topic) {
        return topic.equals(getTopic());
    }

    @Override
    public void handleMessage(String topic, String payload) {
        if (payload == null || payload.isEmpty()) {
            logger.warn("Nhận thẻ RFID bị rỗng từ topic: {}", topic);
            return;
        }

        try {
            User user = userDAO.findByRfidTag(payload);
            AccessLog log = new AccessLog();
            log.setRfidTag(payload);
            
            if (user != null) {
                // Mở cửa nếu thẻ hợp lệ
                mqttService.publishCommand("smarthome/door/control", "OPEN");
                log.setUserId(user.getUserId());
                log.setAccessStatus("SUCCESS");
                logger.info("Thẻ hợp lệ, mở cửa cho user ID: {}", user.getUserId());
            } else {
                log.setUserId(null);
                log.setAccessStatus("DENIED");
                logger.warn("Thẻ RFID không hợp lệ: {}", payload);
            }
            
            accessLogDAO.addAccessLog(log);

            if (dashboard != null) {
                SwingUtilities.invokeLater(() -> {
                    dashboard.loadAccessLogsToTable();
                });
            }
        } catch (Exception e) {
            logger.error("Lỗi khi xử lý thẻ RFID hoặc lưu vào CSDL: {}", e.getMessage(), e);
        }
    }
}