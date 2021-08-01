package me.jeff.ignitepoc.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteClientConfig {

    @Bean
    public Ignite igniteInstance() {
        Ignition.setClientMode(true);
        return Ignition.start();
    }
}
