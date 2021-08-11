package me.jeff.ignitepoc.api;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.cache.CacheCategory;
import me.jeff.ignitepoc.cache.CachePopulation;
import me.jeff.ignitepoc.cache.CacheQuery;
import me.jeff.ignitepoc.cache.StockCache;
import me.jeff.ignitepoc.common.MoIResponse;
import me.jeff.ignitepoc.handler.IgniteCacheHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class IgniteCacheController {
    
    @Autowired
    private IgniteCacheHandler cacheService;

    @Autowired
    private CachePopulation cachePopulator;

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

    @RequestMapping(value = "/populateCache", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse populateCache(@RequestParam String category) {
        if (category.equalsIgnoreCase(CacheCategory.ORG.toString()))
            cachePopulator.populateCache(CacheCategory.ORG);
        else
            cachePopulator.populateCache(CacheCategory.EMP);
        return new MoIResponse("OK", null);
    }

    @Autowired
    private CacheQuery cacheQuery;

    @RequestMapping(value = "/textQuery", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse textQuery(@RequestParam String param) {
        cacheQuery.textQuery(param);
        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/sqlFieldsQuery", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse sqlFieldsQuery(@RequestParam String param) {
        cacheQuery.sqlFieldsQuery();
        return new MoIResponse("OK", null);
    }

    @RequestMapping(value = "/sqlJoinQuery", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse sqlJoinQuery(@RequestParam String param) {
        cacheQuery.sqlJoinQuery();
        return new MoIResponse("OK", null);
    }
}
