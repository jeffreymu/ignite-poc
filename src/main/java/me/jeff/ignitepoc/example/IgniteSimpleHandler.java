package me.jeff.ignitepoc.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class IgniteSimpleHandler {

    private final static String CITY_CACHE = "SQL_PUBLIC_MYCITY";
    private final static String PERSON_CACHE = "SQL_PUBLIC_PERSON";

    @Autowired
    private Ignite ignite;

    private IgniteCache myCityCache;
    private IgniteCache personCache;

    @PostConstruct
    public void initCache() {
        if (ignite == null) {
            log.error("Can't connect to ignite cache");
            throw new RuntimeException("Found nothing ignite cache");
        }
        this.myCityCache = ignite.cache(CITY_CACHE);
        this.personCache = ignite.cache(PERSON_CACHE);
    }

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

    public static void main(String[] args) throws InterruptedException {
        IgniteSimpleHandler handler = new IgniteSimpleHandler();
        handler.init();

        TimeUnit.SECONDS.sleep(30);
    }

}
