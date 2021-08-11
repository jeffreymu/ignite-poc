package me.jeff.ignitepoc.api;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.common.MoIResponse;
import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RestController
public class ComputeCallableController {

    @Autowired
    private Ignite ignite;

    @RequestMapping(value = "/compute", method = RequestMethod.GET, produces = "application/json")
    public MoIResponse computeTest() {
        this.doCompute("Count characters using callable");
        return new MoIResponse("OK", null);
    }
    
    private void doCompute(String words) {
        Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
        for (String word : words.split(" ")) {
            calls.add(() -> {
                log.info(">>> Printing '" + word + "' on this node from ignite job.");
                return word.length();
            });
        }

        Collection<Integer> res = ignite.compute().call(calls);
        int sum = res.stream().mapToInt(i -> i).sum();
        log.info(">>> Total number of characters in the phrase is '" + sum + "'.");
        log.info(">>> Check all nodes for output (this node is also part of the cluster).");
    }
}
