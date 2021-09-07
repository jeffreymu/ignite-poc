package me.jeff.ignitepoc.chronicle;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.chronicle.common.FieldSelector;
import me.jeff.ignitepoc.chronicle.order.CrossingOrder;
import me.jeff.ignitepoc.chronicle.order.CrossingOrderReadMarshallable;
import me.jeff.ignitepoc.chronicle.order.CrossingOrderWriteMarshallable;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.WriteMarshallable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CrossingOrderWriteMarshallableTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private ChronicleQueue chronicleQueue;
    private CrossingOrder crossingOrder;

    @Before
    public void before() {
        chronicleQueue = SingleChronicleQueueBuilder.single(temporaryFolder.getRoot()).blockSize(1024).build();

        crossingOrder = CrossingOrder
                .builder()
                .orderID("2021090600001")
                .portfolioID(1102)
                .strategyID(9001)
                .accountNo("1045_001")
                .build();

    }

    @After
    public void after() {
        chronicleQueue.close();
    }

    @Test
    public void writeReadSingle() throws Exception {

        writeCrossingOrderToChronicle(this.crossingOrder, FieldSelector.DEFAULT_FIELDS);

        CrossingOrder _crossingOrder = readCrossingOrderFromChronicle();

        assertThat(_crossingOrder.getOrderID()).isEqualTo(crossingOrder.getOrderID());
    }

    @Test
    public void testReadOrder() {
        CrossingOrder order = readCrossingOrderFromChronicle();
        log.info(order.toString());
    }

    private void writeCrossingOrderToChronicle(CrossingOrder crossingOrder, FieldSelector fields) {
        WriteMarshallable writeMarshallable = new CrossingOrderWriteMarshallable(crossingOrder, fields);

        ExcerptAppender appender = chronicleQueue.acquireAppender();
        appender.writeDocument(writeMarshallable);
    }

    private CrossingOrder readCrossingOrderFromChronicle() {
        CrossingOrderReadMarshallable readMarshallable = new CrossingOrderReadMarshallable();
        ExcerptTailer tailer = chronicleQueue.createTailer();
        tailer.readDocument(readMarshallable);
        return readMarshallable.getCrossingOrder();
    }

}
