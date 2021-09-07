package me.jeff.ignitepoc.chronicle;

import me.jeff.ignitepoc.chronicle.common.ToolOptions;
import me.jeff.ignitepoc.chronicle.order.CrossingOrder;
import me.jeff.ignitepoc.chronicle.order.CrossingOrderReadMarshallable;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

public class QueueReader {

    private final ExcerptTailer tailer;

    private CrossingOrder nextRecord;

    public QueueReader(ToolOptions toolOptions) {
        this(toolOptions, getChronicleQueue(toolOptions));
    }

    // Visible for testing
    QueueReader(ToolOptions toolOptions, ChronicleQueue chronicleQueue) {
        tailer = getExcerptTailer(toolOptions, chronicleQueue);
    }

    private static ChronicleQueue getChronicleQueue(ToolOptions toolOptions) {
        SingleChronicleQueueBuilder chronicleBuilder = SingleChronicleQueueBuilder.single(toolOptions.path().toFile())
                .readOnly(true);
        toolOptions.rollCycle().ifPresent(chronicleBuilder::rollCycle);

        try {
            return chronicleBuilder.build();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage()); // NOPMD
            System.exit(2); // NOPMD
            return null;
        }
    }

    private static ExcerptTailer getExcerptTailer(ToolOptions toolOptions, ChronicleQueue chronicle) {
        ExcerptTailer tempTailer = chronicle.createTailer();

        if (toolOptions.tail().isPresent()) {
            long startIndex = tempTailer.index();

            tempTailer = tempTailer.toEnd();

            long newIndex = tempTailer.index() - toolOptions.tail().get();
            newIndex = Math.max(newIndex, startIndex);

            tempTailer.moveToIndex(newIndex);
        }

        return tempTailer;
    }

    public boolean hasRecordAvailable() {
        maybeReadNext();
        return nextRecord != null;
    }

    private void maybeReadNext() {
        if (nextRecord == null) {
            readNext();
        }
    }

    private void readNext() {
        CrossingOrderReadMarshallable recordMarshallable = new CrossingOrderReadMarshallable();
        if (tailer.readDocument(recordMarshallable)) {
            nextRecord = recordMarshallable.getCrossingOrder();
        }
    }

    public CrossingOrder nextRecord() {
        maybeReadNext();
        CrossingOrder entry = nextRecord;
        nextRecord = null;
        return entry;
    }

}
