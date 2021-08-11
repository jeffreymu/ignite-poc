package me.jeff.ignitepoc.grid;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public final class DataGridNodeAdmin {

    @Autowired
    private DataGridNodeAdmin(Ignite ignite) {
        this.ignite = ignite;
    }

    private Ignite ignite;

    public void terminalNode() {
        if (ignite != null) {
            ignite.close();
        }
        log.info("Ignite data node is closed " + ignite.name());
    }


}
