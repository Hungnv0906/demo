package com.mycompany.smart_home.dao;

import com.mycompany.smart_home.models.AccessLog;
import java.util.List;

public interface AccessLogDAO {
    boolean addAccessLog(AccessLog log);
    List<AccessLog> getAllAccessLogs();
    List<AccessLog> getLogsByUser(int userId);
}