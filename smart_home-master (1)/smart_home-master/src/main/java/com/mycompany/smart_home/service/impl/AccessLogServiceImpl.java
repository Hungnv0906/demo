package com.mycompany.smart_home.service.impl;

import com.mycompany.smart_home.models.AccessLog;
import com.mycompany.smart_home.models.User;
import com.mycompany.smart_home.service.AccessLogService;
import com.mycompany.smart_home.dao.AccessLogDAO;
import com.mycompany.smart_home.dao.UserDAO;
import com.mycompany.smart_home.dao.impl.AccessLogDAOImpl;
import com.mycompany.smart_home.dao.impl.UserDAOImpl;

import java.util.List;

public class AccessLogServiceImpl implements AccessLogService {
    private AccessLogDAO accessLogDAO = new AccessLogDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public List<AccessLog> getAllAccessHistory(){
        return accessLogDAO.getAllAccessLogs();
    }

    @Override
    public boolean recordAccess(String rfidTag, boolean isSuccess){
        AccessLog log = new AccessLog();
        log.setRfidTag(rfidTag);

        String accessStatus = isSuccess ? "SUCCESS" : "DENIED";
        log.setAccessStatus(accessStatus);

        User user = userDAO.findByRfidTag(rfidTag);

        if(user != null){
            log.setUserId(user.getUserId());
        }else{
            log.setUserId(null);
        }
        return accessLogDAO.addAccessLog(log);
    }
}
