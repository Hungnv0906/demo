package com.mycompany.smart_home.service.mqtt;

public interface MqttMessageHandler {
    // Trả về chuỗi topic để MqttService mang đi subscribe
    String getTopic();
    
    // Kiểm tra xem Handler này có chịu trách nhiệm xử lý topic truyền vào hay không
    boolean canHandle(String topic);
    
    // Thực thi logic nghiệp vụ khi có tin nhắn
    void handleMessage(String topic, String payload);
}