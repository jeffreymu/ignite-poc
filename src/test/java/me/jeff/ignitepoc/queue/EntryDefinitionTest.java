package me.jeff.ignitepoc.queue;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.chronicle.order.CrossingOrder;
import me.jeff.ignitepoc.chronicle.order.Order;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EntryDefinitionTest {

    @Test
    public void test_write_to_queue() throws Exception {
        TempChronicleQueue queue = new TempChronicleQueue();
        ExcerptAppender appender = queue.acquireAppender();
        ExcerptTailer tailer = queue.createTailer();

    }

    private CrossingOrder build_crossing_order() {
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
                .orderID("2021090600001")
                .portfolioID(1102)
                .strategyID(9001)
                .accountNo("1045_001")
                .subOrders(subOrders)
                .build();
    }
}
