package com.mycompany.smart_home.service;

import com.mycompany.smart_home.service.mqtt.MqttMessageHandler;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MqttService implements MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    private MqttClient client;
    private String brokerUrl;
    private String clientId;
    
    private List<MqttMessageHandler> handlers = new CopyOnWriteArrayList<>();

    public MqttService(String brokerUrl, String clientId) {
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
    }

    public MqttService() {
        this("tcp://localhost:1883", "Java_Desktop_App_" + System.currentTimeMillis()); 
    }

    public void addHandler(MqttMessageHandler handler) {
        this.handlers.add(handler);
        if (isConnected()) {
            try {
                client.subscribe(handler.getTopic());
                logger.info("Đã subscribe topic: {}", handler.getTopic());
            } catch (MqttException e) {
                logger.error("Lỗi khi subscribe topic mới: " + handler.getTopic(), e);
            }
        }
    }

    public void start() {
        if (isConnected()) {
            logger.info("MQTT Client đã được kết nối, bỏ qua khởi tạo lại.");
            return;
        }
        try {
            client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true); 
            
            client.setCallback(this);
            client.connect(options);
            logger.info("Đã kết nối thành công tới MQTT Broker: {}", brokerUrl);

            for (MqttMessageHandler handler : handlers) {
                client.subscribe(handler.getTopic());
                logger.info("Đã subscribe topic: {}", handler.getTopic());
            }

            client.subscribe("smarthome/device/+/status");

        } catch (MqttException e) {
            logger.error("Lỗi khi khởi tạo và kết nối MQTT Broker", e);
        }
    }

    public void stop() {
        try {
            if (isConnected()) {
                client.disconnect();
                client.close();
                logger.info("Đã ngắt kết nối MQTT Broker.");
            }
        } catch (MqttException e) {
            logger.error("Lỗi khi ngắt kết nối MQTT Broker", e);
        }
    }

    public void publishCommand(String topic, String command) {
        if (!isConnected()) {
            logger.warn("Không thể gửi lệnh: MQTT Client chưa kết nối.");
            return;
        }
        try {
            MqttMessage message = new MqttMessage(command.getBytes(StandardCharsets.UTF_8));
            message.setQos(1);
            client.publish(topic, message);
            logger.info("Đã gửi lệnh tới topic '{}': {}", topic, command);
        } catch (MqttException e) {
            logger.error("Lỗi khi gửi lệnh tới MQTT Broker tới topic: {}", topic, e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("Mất kết nối tới MQTT Broker. Tính năng tự reconnect có thể đang hoạt động.", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8).trim();
        logger.info("Nhận tin nhắn từ topic '{}': {}", topic, payload);

        for (MqttMessageHandler handler : handlers) {
            if (handler.canHandle(topic)) {
                try {
                    handler.handleMessage(topic, payload);
                } catch (Exception e) {
                    logger.error("Lỗi tại handler khi xử lý topic: " + topic, e);
            }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.debug("Đã gửi tin nhắn thành công qua token: {}", token.getMessageId());
    }
    
    public boolean isConnected() { return client != null && client.isConnected(); }
}