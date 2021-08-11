package me.jeff.ignitepoc.cache;

import me.jeff.ignitepoc.examples.model.Organization;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrgCacheConfigFactory implements CacheConfigBuilder<Integer, Organization> {

    @Override
    public CacheConfiguration createCache(String cacheName, Integer key ,Organization org) {
        CacheConfiguration<Integer, Organization> cacheCfg = new CacheConfiguration<>();
        cacheCfg.setCacheMode(CacheMode.PARTITIONED);
        cacheCfg.setName(cacheName);
        cacheCfg.setQueryEntities(Arrays.asList(createOrganizationQueryEntity()));
        return cacheCfg;
    }

    private static QueryEntity createOrganizationQueryEntity() {
        return new QueryEntity()
                .setValueType(Organization.class.getName())
                .setKeyType(Integer.class.getName())
                .addQueryField("keyId", Integer.class.getName(), null)
                .addQueryField("name", String.class.getName(), null)
                .addQueryField("address.street", String.class.getName(), null)
                .setKeyFieldName("keyId")
                .setIndexes(Arrays.asList(new QueryIndex("name")));
    }

    public String getCacheName() {
        return  OrgCacheConfigFactory.class.getSimpleName()
                + "Organizations";
    }
}
