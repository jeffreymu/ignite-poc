package me.jeff.ignitepoc.config;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteClientConfig {

    @Value("${ignite.remote.host}")
    private String igniteHost;

    @Value("${ignite.login.user}")
    private String loginUser;

    @Value("${ignite.login.password}")
    private String loginPassword;

    @Bean
    public IgniteClient igniteClientInstance() {
        ClientConfiguration cfg = new ClientConfiguration()
                .setAddresses(this.igniteHost)
                .setUserName(this.loginUser)
                .setUserPassword(this.loginPassword);

        Ignition.setClientMode(true);
        return Ignition.startClient(cfg);
    }
}
