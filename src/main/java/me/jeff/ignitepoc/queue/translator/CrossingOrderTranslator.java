package me.jeff.ignitepoc.queue.translator;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;


public class CrossingOrderTranslator implements Runnable {

    private SingleChronicleQueue queue;
    String pathfr = "test-queue-fr";
    OrderConsumer consumer;
    String path_en = "text-queue-en";

    public CrossingOrderTranslator(SingleChronicleQueue queue) {
        this.queue = queue;
        init();
    }

    private void init() {
        SingleChronicleQueue queuefr = SingleChronicleQueueBuilder.binary(pathfr).build();
        OrderConsumer orderConsumer = queuefr.acquireAppender().methodWriter(OrderConsumer.class);
        this.consumer = new CrossingOrderListener(orderConsumer);
    }

    @Override
    public void run() {
        SingleChronicleQueue queue_en = SingleChronicleQueueBuilder.binary(path_en).build();
        MethodReader methodReader = queue_en.createTailer().methodReader(consumer);

        while (true) {
            if (!methodReader.readOne()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
