package com.mycompany.smart_home.service;

import com.mycompany.smart_home.models.AccessLog;

import java.util.List;

public interface AccessLogService {
    List<AccessLog> getAllAccessHistory();
    boolean recordAccess(String rfidTag, boolean isSuccess);
}
