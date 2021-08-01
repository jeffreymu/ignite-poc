package me.jeff.ignitepoc.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class IgniteSimpleHandler {

    private final static String CITY_CACHE = "SQL_PUBLIC_CITY";
    private final static String PERSON_CACHE = "SQL_PUBLIC_PERSON";

    @Autowired
    private Ignite ignite;

    private IgniteCache cityCache;
    private IgniteCache personCache;

    @PostConstruct
    public void initCache() {
        if (ignite == null) {
            log.error("Can't connect to ignite cache");
            throw new RuntimeException("Found nothing ignite cache");
        }
        this.cityCache = ignite.cache(CITY_CACHE);
        this.personCache = ignite.cache(PERSON_CACHE);
    }

    public void init() {

        try (Ignite ignite = Ignition.start()) {
            Ignition.setClientMode(true);
            log.info("Ignite client is started.");
            this.cityCache = ignite.cache(CITY_CACHE);
            this.personCache = ignite.cache(PERSON_CACHE);
            // query cache of testing data
            doQuery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doInsert() {
        IgniteDataHelper.cleanup(cityCache, personCache);
        IgniteDataHelper.insertData(cityCache, personCache);
    }

    public void doQuery() {
        IgniteDataHelper.queryData(cityCache);
    }

    public void doUpdate() {
        IgniteDataHelper.updateData(cityCache);
    }

    public void doRemove() {
        IgniteDataHelper.removeData(cityCache);
    }

    public static void main(String[] args) throws InterruptedException {
        IgniteSimpleHandler handler = new IgniteSimpleHandler();
        handler.init();
    }

}
