package me.jeff.ignitepoc.chronicle;

import me.jeff.ignitepoc.chronicle.common.FieldSelector;
import me.jeff.ignitepoc.chronicle.common.ToolOptions;
import me.jeff.ignitepoc.chronicle.order.CrossingOrder;
import me.jeff.ignitepoc.chronicle.order.CrossingOrderWriteMarshallable;
import me.jeff.ignitepoc.chronicle.order.Order;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.ReadMarshallable;
import net.openhft.chronicle.wire.WriteMarshallable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class QueueReaderTest {

    private ChronicleQueue queue;
    private ExcerptTailer tailer;
    private CrossingOrder crossingOrder;
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void before() throws UnknownHostException {

        crossingOrder = CrossingOrder
                .builder()
                .orderID(String.valueOf(System.currentTimeMillis()))
                .portfolioID(1102)
                .strategyID(9001)
                .accountNo("1045_001")

                .build();
        queue = SingleChronicleQueueBuilder.single(temporaryFolder.getRoot()).blockSize(1024).build();

//        when(queue.createTailer()).thenReturn(tailer);
//        when(tailer.toEnd()).thenReturn(tailer);
//        when(tailer.index()).thenReturn(0L).thenReturn(1000L);
    }

    private QueueReader givenReader() {
        return givenReader(ToolOptions.builder().build());
    }

    private QueueReader givenReader(ToolOptions toolOptions) {
        return new QueueReader(toolOptions, queue);
    }

    @Test
    public void testNothingToRead() {
        when(tailer.readDocument(any(ReadMarshallable.class))).thenReturn(false);
        QueueReader reader = givenReader();
        assertThat(reader.hasRecordAvailable()).isFalse();
    }

    @Test
    public void testValidSingleRecord() throws UnknownHostException {
        QueueReader reader = givenReader();
        List<Order> subOrders = new ArrayList<>();
        subOrders.add(Order.builder()
                .accountNo("11").combiNo("123").orderID("100").parentOrderID("001")
                .portfolioID(1102).strategyID(9001)
                .build());
        subOrders.add(Order.builder()
                .accountNo("12").combiNo("223").orderID("101").parentOrderID("002")
                .portfolioID(1102).strategyID(9001)
                .build());
        writeCrossingOrderToChronicle(CrossingOrder
                .builder()
                .orderID("2021090600001")
                .portfolioID(1102)
                .strategyID(9001)
                .accountNo("1045_001")
                .subOrders(subOrders)
                .build(), FieldSelector.DEFAULT_FIELDS);
        readOrder(reader);
    }

    private void readOrder(QueueReader reader) {
        assertThat(reader.hasRecordAvailable()).isTrue();
        if (reader.hasRecordAvailable()) {
            CrossingOrder nextOrder = reader.nextRecord();
            System.out.println(nextOrder.toString());
        }
    }

    @Test
    public void testNothingToReadDirect() {
        when(tailer.readDocument(any(ReadMarshallable.class))).thenReturn(false);
        QueueReader reader = givenReader();
        assertThat(reader.nextRecord()).isNull();
    }

    private void writeCrossingOrderToChronicle(CrossingOrder crossingOrder, FieldSelector fields) {
        WriteMarshallable writeMarshallable = new CrossingOrderWriteMarshallable(crossingOrder, fields);

        ExcerptAppender appender = queue.acquireAppender();
        appender.writeDocument(writeMarshallable);
    }
}
