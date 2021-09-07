package me.jeff.ignitepoc.chronicle;

import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.junit.Test;

public class ChronicleQueueTest {

    @Test
    public void write_bytes() throws Exception{
        String path = "test-queue-poc-daily";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.DAILY).build();
        ExcerptAppender appender = queue.acquireAppender();


    }
}
