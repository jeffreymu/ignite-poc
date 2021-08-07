package me.jeff.ignitepoc.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class IgniteSimpleHandler {

    private final static String CITY_CACHE = "SQL_PUBLIC_MYCITY";
    private final static String PERSON_CACHE = "SQL_PUBLIC_PERSON";

    private Ignite ignite;
    private IgniteClient igniteClient;

    private IgniteCache myCityCache;
    private IgniteCache personCache;

    private ClientCache myCityClientCache;
    private ClientCache personClientCache;

    @Autowired
    public IgniteSimpleHandler(IgniteClient igniteClient) {
        this.igniteClient = igniteClient;
    }

    @PostConstruct
    public void initCache() {
        if (igniteClient == null) {
            log.error("Can't connect to ignite cache");
            throw new RuntimeException("Found nothing ignite cache");
        }
        this.myCityClientCache = igniteClient.cache(CITY_CACHE);
        this.personClientCache = igniteClient.cache(PERSON_CACHE);
        log.info("Started the ignite thin client");
    }

    @Deprecated
    public void init() {
        try (Ignite ignite = Ignition.start()) {
            Ignition.setClientMode(true);
            log.info("Ignite client is started.");
            this.myCityCache = ignite.cache(CITY_CACHE);
            this.personCache = ignite.cache(PERSON_CACHE);
            // query cache of testing data
            doQuery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doInsert() {
        IgniteDataHelper.cleanup(myCityCache, personCache);
        IgniteDataHelper.insertData(myCityCache, personCache);
    }

    public void doQuery() {
        IgniteDataHelper.queryData(myCityCache);
    }

    public void doUpdate() {
        IgniteDataHelper.updateData(myCityCache);
    }

    public void doRemove() {
        IgniteDataHelper.removeData(myCityCache);
    }

    /***********************************************************************/

    public void doClientInsert() {
        IgniteClientDataHelper.cleanup(myCityClientCache, personClientCache);
        IgniteClientDataHelper.insertData(myCityClientCache, personClientCache);
    }

    public void doClientQuery() {
        IgniteClientDataHelper.queryData(myCityClientCache);
    }

}
