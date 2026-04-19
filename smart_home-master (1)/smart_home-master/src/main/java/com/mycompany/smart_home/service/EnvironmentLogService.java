package com.mycompany.smart_home.service;

import com.mycompany.smart_home.models.EnvironmentLog;

import java.util.List;

public interface EnvironmentLogService {
    boolean recordLog(float temperature, float humidity);
    List<EnvironmentLog> getLogsForChart(int limit);
    boolean clearOldData(int daysLimit);
}