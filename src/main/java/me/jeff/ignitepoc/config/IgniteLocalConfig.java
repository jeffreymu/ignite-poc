package me.jeff.ignitepoc.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableIgniteRepositories
public class IgniteLocalConfig {

    @Bean
    public Ignite igniteInstance() {
        String cfgPath = "config/example-persistent-store.xml";
        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start(cfgPath);
        log.info("Ignite data node is startup " + ignite.name());

//        // cache configuration
//        CacheConfiguration alerts=new CacheConfiguration();
//        alerts.setCopyOnRead(false);
//        // as we have one node for now
//        alerts.setBackups(1);
//        alerts.setAtomicityMode(CacheAtomicityMode.ATOMIC);
//        alerts.setName(CacheNames.Alerts.name());
//        alerts.setDataRegionName("AlertRegionConfiguration");
//        alerts.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
//        alerts.setIndexedTypes(String.class,AlertEntry.class);
//
//        CacheConfiguration alertsConfig=new CacheConfiguration();
//        alertsConfig.setCopyOnRead(false);
//        // as we have one node for now
//        alertsConfig.setBackups(1);
//        alertsConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);
//        alertsConfig.setName(CacheNames.AlertsConfig.name());
//        alertsConfig.setIndexedTypes(String.class,AlertConfigEntry.class);
//        alertsConfig.setDataRegionName("AlertRegionConfiguration");
//        alertsConfig.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
//
//        ignite.getOrCreateCache(alerts);
//        ignite.getOrCreateCache(alertsConfig);
        return ignite;
    }

//    @Bean(name = "igniteInstance")
//    public Ignite igniteInstance(Ignite ignite) {
//        return ignite;
//    }

//    @Bean
//    public IgniteConfigurer configurer() {
//        return igniteConfiguration -> {
//            igniteConfiguration.setClientMode(true);
//        };
//    }

}
