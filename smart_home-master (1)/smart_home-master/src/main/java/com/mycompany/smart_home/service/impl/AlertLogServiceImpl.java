package com.mycompany.smart_home.service.impl;

import com.mycompany.smart_home.dao.AlertLogDAO;
import com.mycompany.smart_home.dao.impl.AlertLogDAOImpl;
import com.mycompany.smart_home.models.AlertLog;
import com.mycompany.smart_home.service.AlertLogService;
import java.util.List;

public class AlertLogServiceImpl implements AlertLogService {
    private AlertLogDAO alertLogDAO = new AlertLogDAOImpl();

    @Override
    public boolean recordAlert(String type, String message, float threshold, float actualValue) {
        AlertLog alert = new AlertLog();
        alert.setAlertType(type);
        alert.setMessage(message);
        alert.setThreshold(threshold);
        alert.setActualValue(actualValue);
        return alertLogDAO.addAlert(alert);
    }

    @Override
    public List<AlertLog> getAllAlertLogs() {
        return alertLogDAO.getAllAlerts();
    }
}