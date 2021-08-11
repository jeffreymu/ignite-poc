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
        String cfgPath = "config/example-cache.xml";
        Ignite ignite = Ignition.start(cfgPath);
        log.info("Ignite data node is startup " + ignite.name());
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
