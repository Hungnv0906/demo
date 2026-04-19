package com.mycompany.smart_home.dao;

import com.mycompany.smart_home.models.EnvironmentLog;
import java.util.List;

public interface EnvironmentLogDAO {
    boolean addLog(EnvironmentLog log);
    List<EnvironmentLog> getLatestLogs(int limit);
    boolean deleteOldLogs(int daysLimit);
}