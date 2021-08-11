package me.jeff.ignitepoc.grid;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

@Slf4j
public final class DataGridNodeStartup {

    public static void startup() {
        Ignite ignite = Ignition.start("config/example-cache.xml");
        log.info("Ignite data node is startup " + ignite.name());
    }

    public static void main(String[] args) throws IgniteException {
        DataGridNodeStartup.startup();
    }
}
