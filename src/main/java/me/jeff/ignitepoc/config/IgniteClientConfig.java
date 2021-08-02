package me.jeff.ignitepoc.config;

import org.apache.ignite.Ignite;
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

//    @Bean
    public Ignite igniteInstance() {
//        IgniteConfiguration cfg = new IgniteConfiguration();
//        cfg.setLocalHost("127.0.0.1");
//        cfg.setPeerClassLoadingEnabled(true);
//        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
//        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
//        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
//        discoSpi.setIpFinder(ipFinder);
//        cfg.setDiscoverySpi(discoSpi);

//        TcpDiscoverySpi spi = new TcpDiscoverySpi();
//        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
//        ipFinder.setMulticastGroup("127.0.0.1");
//        ipFinder.setAddresses(Arrays.asList("127.0.0.1.47500..47509"));
//        spi.setLocalPort(47508);
//        spi.setLocalPortRange(0);
//        spi.setIpFinder(ipFinder);
//        TcpCommunicationSpi commSpi=new TcpCommunicationSpi();
//        commSpi.setLocalPort(47509);
//        cfg.setDiscoverySpi(spi);
//        cfg.setCommunicationSpi(commSpi);

        Ignition.setClientMode(true);
        return Ignition.start();
    }

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
