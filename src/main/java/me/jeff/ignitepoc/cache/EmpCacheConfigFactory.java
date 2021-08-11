package me.jeff.ignitepoc.cache;

import me.jeff.ignitepoc.examples.model.Employee;
import me.jeff.ignitepoc.examples.model.EmployeeKey;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.*;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class EmpCacheConfigFactory implements CacheConfigBuilder<EmployeeKey, Employee> {

    @Autowired
    private Ignite ignite;

    @Override
    public CacheConfiguration createCache(String cacheName, EmployeeKey key, Employee employee) {
        CacheConfiguration<EmployeeKey, Employee> employeeCacheCfg = new CacheConfiguration<>();
        employeeCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        employeeCacheCfg.setName(cacheName);
        employeeCacheCfg.setQueryEntities(Arrays.asList(createEmployeeQueryEntity()));
        employeeCacheCfg.setKeyConfiguration(new CacheKeyConfiguration(EmployeeKey.class));
        return employeeCacheCfg;
    }

    public IgniteCache getIgniteCache() {
        IgniteCache<EmployeeKey, Employee>  employeeCache = ignite.getOrCreateCache(getCacheName());
        IgniteCache<BinaryObject, BinaryObject> binaryCache = employeeCache.withKeepBinary();
        return binaryCache;
    }

    /**
     * Create cache type metadata for {@link Employee}.
     *
     * @return Cache type metadata.
     */
    private static QueryEntity createEmployeeQueryEntity() {
        return new QueryEntity()
                .setValueType(Employee.class.getName())
                .setKeyType(EmployeeKey.class.getName())
                .addQueryField("organizationId", Integer.class.getName(), null)
                .addQueryField("name", String.class.getName(), null)
                .addQueryField("salary", Long.class.getName(), null)
                .addQueryField("addr.zip", Integer.class.getName(), null)
                .addQueryField("addr.street", String.class.getName(), null)
                .setKeyFields(Collections.singleton("organizationId"))
                .setIndexes(Arrays.asList(
                        new QueryIndex("name"),
                        new QueryIndex("salary"),
                        new QueryIndex("addr.zip"),
                        new QueryIndex("organizationId"),
                        new QueryIndex("addr.street", QueryIndexType.FULLTEXT)));
    }

    public String getCacheName() {
        return  EmpCacheConfigFactory.class.getSimpleName()
                + "Employees";
    }
}
