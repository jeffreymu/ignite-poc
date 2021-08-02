package me.jeff.ignitepoc.service;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.exception.ResourceNotFoundException;
import me.jeff.ignitepoc.model.AlertEntry;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class IgniteAlertsStore implements AlertsStore {

//    @Autowired
    private Ignite ignite;

    @Override
    public List<AlertEntry> getAlertForServiceId(String serviceId) {
        final String sql = "serviceId = ?";
        SqlQuery<String, AlertEntry> query = new SqlQuery<>(AlertEntry.class, sql);
        query.setArgs(serviceId);
        return Optional.ofNullable(getAlertsCache().query(query).getAll()
                .stream()
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Alert for %s not found", serviceId)));
    }

    @Override
    public void updateAlertEntry(String serviceId, String serviceCode, AlertEntry alertEntry) {
        final IgniteCache<String, AlertEntry> alertsCache = getAlertsCache();
        // update the alert entry via cache invoke for atomicity
        alertsCache.invoke(alertEntry.getAlertId(), (mutableEntry, objects) -> {
            if (mutableEntry.exists() && mutableEntry.getValue() != null) {
                log.debug("updating alert entry into the cache store invoke: {},{}", serviceId, serviceCode);
                mutableEntry.setValue(alertEntry);
            } else {
                throw new ResourceNotFoundException(String.format("Alert for %s with %s not found", serviceId, serviceCode));
            }
            // by api design nothing needed here
            return null;
        });
    }

    @Override
    public List<AlertEntry> getAllAlerts() {
        final String sql = "select * from AlertEntry";
        SqlQuery<String, AlertEntry> query = new SqlQuery<>(AlertEntry.class, sql);
        return getAlertsCache().query(query).getAll()
                .stream()
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteAlertEntry(String alertId) {
        final IgniteCache<String, AlertEntry> alertsCache = getAlertsCache();
        alertsCache.remove(alertId);
    }

    @Override
    public void createAlertEntry(AlertEntry alertEntry) {
        // get the max count of alerts before sending mail
        final int maxCount = 100;
        final String mailTemplate = "ticket";
        // define the expiry of the entry in the cache
        final IgniteCache<String, AlertEntry> alertsCache = getAlertsCache();
        // insert into the key value store
        alertsCache.put(alertEntry.getAlertId(), alertEntry);
        // send the mail notification if max is there
        final SqlFieldsQuery sql = new SqlFieldsQuery("select count(*) from AlertEntry where serviceId = '" + alertEntry.getServiceId() + "' and errorCode = '" + alertEntry.getErrorCode() + "'");
        final List<List<?>> count = alertsCache.query(sql).getAll();
        if (count != null && !count.isEmpty()) {
            final Long result = (Long) count.get(0).get(0);
            if (result >= maxCount) {
                log.debug("max alerts count is reached for : {}, start sending mail alert {}", alertEntry.toString());
            }
        }
    }


    public IgniteCache<String, AlertEntry> getAlertsCache() {
        return ignite.cache(CacheNames.Alerts.name());
    }
}
