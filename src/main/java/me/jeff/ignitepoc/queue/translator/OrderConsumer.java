package me.jeff.ignitepoc.queue.translator;

import me.jeff.ignitepoc.chronicle.order.CrossingOrder;

public interface OrderConsumer {

    void onOrder(CrossingOrder order);
}
