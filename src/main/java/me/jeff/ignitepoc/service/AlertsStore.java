package me.jeff.ignitepoc.service;

import me.jeff.ignitepoc.model.AlertEntry;

import java.util.List;

public interface AlertsStore {

    List<AlertEntry> getAlertForServiceId(String serviceId);

    void updateAlertEntry(String serviceId, String errorCode, AlertEntry alertEntry);

    List<AlertEntry> getAllAlerts();

    void deleteAlertEntry(String alertId);

    void createAlertEntry(AlertEntry alertEntry);

}
