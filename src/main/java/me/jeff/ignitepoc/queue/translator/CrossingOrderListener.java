package me.jeff.ignitepoc.queue.translator;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.chronicle.order.CrossingOrder;

@Slf4j
public class CrossingOrderListener implements OrderConsumer {

    private OrderConsumer out;

    public CrossingOrderListener(OrderConsumer consumer) {
        this.out = consumer;
    }

    @Override
    public void onOrder(CrossingOrder order) {
        log.info("crossing order => " + order.toString());
        out.onOrder(order);
    }
}
