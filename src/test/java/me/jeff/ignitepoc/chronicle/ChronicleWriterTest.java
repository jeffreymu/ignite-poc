package me.jeff.ignitepoc.chronicle;

import me.jeff.ignitepoc.chronicle.common.FieldSelector;
import me.jeff.ignitepoc.chronicle.order.CrossingOrder;
import me.jeff.ignitepoc.chronicle.order.CrossingOrderWriteMarshallable;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.WriteMarshallable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ChronicleWriterTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private ChronicleQueue chronicleQueue;
    private ChronicleWriter writer;

    @Before
    public void before() {
        chronicleQueue = SingleChronicleQueueBuilder.single(temporaryFolder.getRoot()).blockSize(1024).build();
        writer = new ChronicleWriter(chronicleQueue);
    }


    @Test
    public void putOneAndClose() throws Exception {
        WriteMarshallable writeMarshallable = new CrossingOrderWriteMarshallable(CrossingOrder
                .builder()
                .orderID("2021090600001")
                .portfolioID(1102)
                .strategyID(9001)
                .accountNo("1045_001")
                .build(), FieldSelector.DEFAULT_FIELDS);
        writer.put(writeMarshallable);
        writer.close();
    }
}
