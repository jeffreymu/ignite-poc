package me.jeff.ignitepoc.cache;

import me.jeff.ignitepoc.examples.model.Address;
import me.jeff.ignitepoc.examples.model.Organization;
import me.jeff.ignitepoc.examples.model.OrganizationType;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.sql.Timestamp;

public class OrgCacheObj implements CustomCache<Integer, Organization> {

    public static void create(Ignite ignite, OrgCacheConfigFactory factory) {
        IgniteCache<Integer, Organization>  orgCache = ignite.getOrCreateCache(
                factory.createCache(factory.getCacheName(), null, null));

        orgCache.put(1, new Organization(
                "GridGain",
                new Address("1065 East Hillsdale Blvd, Foster City, CA", 94404),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis())
        ));

        orgCache.put(2, new Organization(
                "Microsoft",
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis())
        ));
    }

}
