package com.mycompany.smart_home.service;

import com.mycompany.smart_home.models.AlertLog;
import java.util.List;

public interface AlertLogService {
    boolean recordAlert(String type, String message, float threshold, float actualValue);
    List<AlertLog> getAllAlertLogs();
}