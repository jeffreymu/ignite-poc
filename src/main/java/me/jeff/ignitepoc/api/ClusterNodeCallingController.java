package me.jeff.ignitepoc.api;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.common.MoIResponse;
import me.jeff.ignitepoc.examples.ExamplesUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cluster.ClusterGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ClusterNodeCallingController {

    @Autowired
    private Ignite ignite;

    @RequestMapping(value = "/calling", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse calling(@RequestParam String host) {
        if (!host.equalsIgnoreCase("host"))
            this.callNode();
        else
            this.callNode4Host();
        return new MoIResponse("OK", null);
    }
    
    private void callNode() {
        if (!ExamplesUtils.checkMinTopologySize(ignite.cluster(), 2))
            return;
        log.info("Compute example started.");
        IgniteCluster cluster = ignite.cluster();
        ClusterGroup randomNode = cluster.forRemotes().forRandom();
        sayHello(ignite, randomNode);
    }

    private void callNode4Host() {
        if (!ExamplesUtils.checkMinTopologySize(ignite.cluster(), 2))
            return;
        log.info("Compute example started.");
        IgniteCluster cluster = ignite.cluster();
        ClusterGroup randomNode = cluster.forRemotes().forRandom();
        sayHello(ignite, cluster.forHost(randomNode.node()));
    }

    /**
     * Print 'Hello' message on remote nodes.
     *
     * @param ignite Ignite.
     * @param grp Cluster group.
     * @throws IgniteException If failed.
     */
    private static void sayHello(Ignite ignite, final ClusterGroup grp) throws IgniteException {
        // Print out hello message on all cluster nodes.
        ignite.compute(grp).broadcast(
                () -> log.info(">>> Hello Node: " + grp.ignite().cluster().localNode().id()));
    }
}
