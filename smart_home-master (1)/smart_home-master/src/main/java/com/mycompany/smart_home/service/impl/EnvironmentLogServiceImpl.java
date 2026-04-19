package com.mycompany.smart_home.service.impl;

import com.mycompany.smart_home.dao.EnvironmentLogDAO;
import com.mycompany.smart_home.dao.impl.EnvironmentLogDAOImpl;
import com.mycompany.smart_home.models.EnvironmentLog;
import com.mycompany.smart_home.service.EnvironmentLogService;

import java.util.List;

public class EnvironmentLogServiceImpl implements EnvironmentLogService {

    private EnvironmentLogDAO logDAO = new EnvironmentLogDAOImpl();

    @Override
    public boolean recordLog(float temperature, float humidity) {
        EnvironmentLog log = new EnvironmentLog();

        log.setTemperature(temperature);
        log.setHumidity(humidity);
    
        return logDAO.addLog(log);
    }

    @Override
    public List<EnvironmentLog> getLogsForChart(int limit) {
        if (limit <= 0) limit = 20;
        return logDAO.getLatestLogs(limit);
    }

    @Override
    public boolean clearOldData(int daysLimit) {
        return logDAO.deleteOldLogs(daysLimit);
    }
}