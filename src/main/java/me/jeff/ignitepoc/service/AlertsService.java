package me.jeff.ignitepoc.service;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.model.AlertEntry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AlertsService {

//    @Autowired
    private AlertsStore alertsStore;

    public void createAlertEntry(AlertEntry alertEntry) {
        log.debug("createAlertEntry service call with {}",alertEntry.toString());
        alertEntry.setAlertId(UUID.randomUUID().toString());
        alertEntry.setTimestamp(System.currentTimeMillis());
        alertsStore.createAlertEntry(alertEntry);

    }

    public List<AlertEntry> getAlertForServiceId(String serviceId) {
        log.debug("GetAlertForServiceId service call with {}",serviceId);
        return alertsStore.getAlertForServiceId(serviceId);
    }


    public void updateAlertEntry(String serviceId, String serviceCode, AlertEntry alertEntry) {
        log.debug("updateAlertEntry service call with {}, {}, {}",serviceId,serviceCode,alertEntry.toString());
        alertsStore.updateAlertEntry(serviceId, serviceCode, alertEntry);
    }


    public List<AlertEntry> getAllAlerts() {
        log.debug("getAllAlerts service call");
        return alertsStore.getAllAlerts();
    }
}
