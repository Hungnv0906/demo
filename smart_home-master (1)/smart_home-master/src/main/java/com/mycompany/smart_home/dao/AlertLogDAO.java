package com.mycompany.smart_home.dao;

import com.mycompany.smart_home.models.AlertLog;
import java.util.List;

public interface AlertLogDAO {
    boolean addAlert(AlertLog alert);
    List<AlertLog> getAllAlerts();
}