package me.jeff.ignitepoc.queue.translator;

import me.jeff.ignitepoc.chronicle.order.CrossingOrder;
import me.jeff.ignitepoc.chronicle.order.Order;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CrossingOrderMain {

    private SingleChronicleQueue queue_en;
    private OrderConsumer orderConsumer;

    @PostConstruct
    private void init() {
        String path_en = "text-queue-en";
        queue_en = SingleChronicleQueueBuilder.binary(path_en).build();
        orderConsumer = queue_en.acquireAppender().methodWriter(OrderConsumer.class);

        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        es.scheduleWithFixedDelay(new CrossingOrderTranslator(queue_en), 2, 10, TimeUnit.SECONDS);
    }

    public CrossingOrder buildOrder(String id) {
        List<Order> subOrders = new ArrayList<>();
        subOrders.add(Order.builder()
                .accountNo("11").combiNo("123").orderID("100").parentOrderID("001")
                .portfolioID(1102).strategyID(9001)
                .build());
        subOrders.add(Order.builder()
                .accountNo("12").combiNo("223").orderID("101").parentOrderID("002")
                .portfolioID(1102).strategyID(9001)
                .build());
        return CrossingOrder
                .builder()
                .orderID(id)
                .portfolioID(1102)
                .strategyID(9001)
                .accountNo("1045_001")
                .subOrders(subOrders)
                .build();
    }

    public void createOne(String orderId) {
        orderConsumer.onOrder(buildOrder(orderId));
    }

//    public static void main(String[] args) {
//        init();
//        orderConsumer.onOrder(buildOrder(String.valueOf(System.currentTimeMillis())));
//        new Thread(new CrossingOrderTranslator(queue_en)).start();
//    }

}
