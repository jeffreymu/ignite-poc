package me.jeff.ignitepoc.config;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteClientConfig {
    private final static String IGNITE_LOCAL_ADDRESS = "127.0.0.1:10800";

    @Value("${ignite.remote.host}")
    private String igniteHost;

    @Value("${ignite.login.user}")
    private String loginUser;

    @Value("${ignite.login.password}")
    private String loginPassword;

    @Value("${ignite.useLocal}")
    private boolean useLocal;

    @Bean
    public IgniteClient igniteClientInstance() {
        ClientConfiguration cfg = null;

        if (useLocal) {
            cfg = new ClientConfiguration()
                    .setAddresses(IGNITE_LOCAL_ADDRESS);
        } else {
            cfg = new ClientConfiguration()
                    .setAddresses(this.igniteHost)
                    .setUserName(this.loginUser)
                    .setUserPassword(this.loginPassword);

        }

        Ignition.setClientMode(true);
        return Ignition.startClient(cfg);
    }
}
