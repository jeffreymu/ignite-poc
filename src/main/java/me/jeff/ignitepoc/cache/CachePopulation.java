package me.jeff.ignitepoc.cache;

import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CachePopulation {

    @Autowired
    private OrgCacheConfigFactory orgCacheConfig;

    @Autowired
    private EmpCacheConfigFactory empCacheConfig;

    @Autowired
    private Ignite ignite;

    public void populateCache( CacheCategory cacheCategory) {
        switch (cacheCategory) {
            case ORG:
                OrgCacheObj.create(ignite, orgCacheConfig);
                break;
            case EMP:
                EmpCacheObj.create(ignite, empCacheConfig);
                break;
            default:
                break;
        }
    }

    public void queryText() {}

}
