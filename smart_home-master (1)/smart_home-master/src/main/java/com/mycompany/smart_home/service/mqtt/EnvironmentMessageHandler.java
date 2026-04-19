package com.mycompany.smart_home.service.mqtt;

import com.mycompany.smart_home.dao.EnvironmentLogDAO;
import com.mycompany.smart_home.dao.impl.EnvironmentLogDAOImpl;
import com.mycompany.smart_home.models.EnvironmentLog;
import com.mycompany.smart_home.service.AlertLogService;
import com.mycompany.smart_home.service.impl.AlertLogServiceImpl;
import com.mycompany.smart_home.ui.MainDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.SwingUtilities;

public class EnvironmentMessageHandler implements MqttMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentMessageHandler.class);
    private EnvironmentLogDAO environmentLogDAO = new EnvironmentLogDAOImpl();
    
    // Khởi tạo Service Cảnh báo mới
    private AlertLogService alertService = new AlertLogServiceImpl();
    private MainDashboard dashboard;

    public EnvironmentMessageHandler(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public String getTopic() {
        return "smarthome/sensor/nhietdo";
    }

    @Override
    public boolean canHandle(String topic) {
        return topic.equals(getTopic());
    }

    @Override
    public void handleMessage(String topic, String payload) {
        try {
            String[] parts = payload.split(",");
            float temperature = Float.parseFloat(parts[0].trim());
            float humidity = parts.length > 1 ? Float.parseFloat(parts[1].trim()) : 0.0f;

            // Lưu log môi trường bình thường
            EnvironmentLog envLog = new EnvironmentLog();
            envLog.setTemperature(temperature);
            envLog.setHumidity(humidity);
            environmentLogDAO.addLog(envLog);
            logger.debug("Đã lưu CSDL - Nhiệt độ: {}°C, Độ ẩm: {}%", temperature, humidity);

            // KIỂM TRA QUÁ NHIỆT VÀ GHI LOG CẢNH BÁO
            if (temperature > 50.0f) {
                alertService.recordAlert("NHIET_DO_CAO", "Cảnh báo vượt ngưỡng nhiệt độ an toàn!", 50.0f, temperature);
                if (dashboard != null) {
                    SwingUtilities.invokeLater(() -> {
                        dashboard.loadAlertLogsToTable(); // Cập nhật bảng
                        javax.swing.JOptionPane.showMessageDialog(dashboard,
                            "NGUY HIỂM: Nhiệt độ hiện tại là " + temperature + "°C!\nVui lòng kiểm tra hệ thống ngay!",
                            "CẢNH BÁO QUÁ NHIỆT",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    });
                }
            }

            // Cập nhật giao diện thông số
            if (dashboard != null) {
                SwingUtilities.invokeLater(() -> {
                    dashboard.updateEnvironmentParametersString(String.valueOf(temperature), String.valueOf(humidity));
                    dashboard.drawEnvironmentalGraph();
                });
            }
        } catch (NumberFormatException ex) {
            logger.error("Lỗi sai định dạng: {}", payload, ex);
        } catch (Exception e) {
            logger.error("Lỗi không xác định: {}", e.getMessage(), e);
        }
    }
}