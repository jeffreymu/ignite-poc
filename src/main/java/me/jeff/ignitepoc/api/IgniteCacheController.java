package me.jeff.ignitepoc.api;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.cache.StockCache;
import me.jeff.ignitepoc.common.MoIResponse;
import me.jeff.ignitepoc.example.IgniteCacheHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class IgniteCacheController {
    
    @Autowired
    private IgniteCacheHandler cacheService;

    @RequestMapping(value = "/createCache", method = RequestMethod.POST, produces = "application/json")
    public MoIResponse create(@RequestParam String cacheName) {
        cacheService.createCache(cacheName);

        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/putCache", method = RequestMethod.POST, produces = "application/json")
    public MoIResponse put(@RequestBody StockCache stock) {
        cacheService.putCache(stock.getCacheName(), stock.getKey(), stock);

        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/getCache", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse get(@RequestParam String cacheName, @RequestParam String key) {
        cacheService.getCache(cacheName, key);

        return new MoIResponse("OK", null);
    }
}
