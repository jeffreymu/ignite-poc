package me.jeff.ignitepoc.handler;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.cache.MoICache;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IgniteCacheHandler {

    private IgniteClient igniteClient;
    
    private ClientCache<String, MoICache> cache;

    @Autowired
    public IgniteCacheHandler(IgniteClient igniteClient) {
        this.igniteClient = igniteClient;
    }

    public void createCache(String cacheName) {
        this.cache = igniteClient
                .getOrCreateCache(cacheName);
        log.info("IGNITE-CACHE>>> Created cache {}.\n", cacheName);
    }

    public void putCache(String cacheName, String key, MoICache cacheObj) {
        this.cache = igniteClient
                .getOrCreateCache(cacheName);
        cache.put(key, cacheObj);
        log.info("IGNITE-CACHE>>> Put cache {}.\n", cacheObj.toString());
    }

    public void getCache(String cacheName, String key) {
        this.cache = igniteClient
                .getOrCreateCache(cacheName);
        MoICache cachedObj = cache.get(key);
        if (cachedObj == null) {
            log.error("Can't find the cache {} in {}", key, cacheName);
            return;
        }
        log.info("IGNITE-CACHE>>> Get cache {}.\n", cachedObj.toString());
    }
    
}
