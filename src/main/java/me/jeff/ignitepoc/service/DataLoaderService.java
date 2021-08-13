package me.jeff.ignitepoc.service;

import me.jeff.ignitepoc.model.AlertConfigEntry;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Cache;

@Service
public class DataLoaderService {

    @Autowired
    private Ignite ignite;

//    @PostConstruct
    public void init() {
        final Cache<String, AlertConfigEntry> alertsConfigCache = ignite.getOrCreateCache(CacheNames.AlertsConfig.name());


    }

}
