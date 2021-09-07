package me.jeff.ignitepoc.queue.micros;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.util.Time;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;

import java.io.File;

public class OrderManagerMain {

    public static void manyEntries() {
        File queuePath = new File(OS.getTarget(), "testManyEntries-" + Time.uniqueId());
        try (SingleChronicleQueue cq = ChronicleQueue.singleBuilder(queuePath)
                .rollCycle(RollCycles.TEST_DAILY)
                .testBlockSize()
                .build()) {
            for (int j = 0; j < 20000; j += 1000) {
                long start = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    cq.tableStorePut("=hello" + (j + i), i);
                }
                long end = System.nanoTime() - start;
                System.out.println(j + ": " + end / 1000 / 1e3 + " us/entry");
            }
        }
    }

    public static void testListener() {
        File queuePath = new File(OS.getTarget(), "testWithQueue-" + Time.uniqueId());
        try (ChronicleQueue queue = ChronicleQueue.singleBuilder(queuePath).testBlockSize().build()) {
            OrderIdeaListener orderManager = queue.acquireAppender().methodWriter(OrderIdeaListener.class, MarketDataListener.class);
            SidedMarketDataCombiner combiner = new SidedMarketDataCombiner((MarketDataListener) orderManager);

            // events in
            orderManager.onOrderIdea(new OrderIdea("EURUSD", Side.Buy, 1.1180, 2e6)); // not expected to trigger

            combiner.onSidedPrice(new SidedPrice("EURUSD", 123456789000L, Side.Sell, 1.1172, 2e6));
            combiner.onSidedPrice(new SidedPrice("EURUSD", 123456789100L, Side.Buy, 1.1160, 2e6));
            combiner.onSidedPrice(new SidedPrice("EURUSD", 123456789100L, Side.Buy, 1.1167, 2e6));

            orderManager.onOrderIdea(new OrderIdea("EURUSD", Side.Buy, 1.1165, 1e6)); // expected to trigger
        }

        try (ChronicleQueue queue = ChronicleQueue.singleBuilder(queuePath).testBlockSize().build()) {
            OrderListener listener = queue.acquireAppender().methodWriter(OrderListener.class);
            OrderManager orderManager = new OrderManager(listener);
            MethodReader reader = queue.createTailer().methodReader(orderManager);
            for (int i = 0; i < 5; i++) {
                System.out.println(reader.readOne());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        testListener();

    }
}
