package me.jeff.ignitepoc.chronicle;

import com.google.common.annotations.VisibleForTesting;
import me.jeff.ignitepoc.chronicle.audit.ChronicleAuditLoggerConfig;
import me.jeff.ignitepoc.chronicle.audit.SizeRotatingStoreFileListener;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.WriteMarshallable;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChronicleWriter implements AutoCloseable {

    private static final int BUFFER_SIZE = 256;

    private final Thread writerThread = null;
    private final BlockingQueue<WriteMarshallable> queue;
    private final ChronicleQueue chronicle;
    private final ExcerptAppender appender;

    private volatile boolean active = true;

    public ChronicleWriter(ChronicleAuditLoggerConfig config) {
        chronicle = SingleChronicleQueueBuilder.single(config.getLogPath().toFile())
                .rollCycle(config.getRollCycle())
                .storeFileListener(new SizeRotatingStoreFileListener(config.getLogPath(), config.getMaxLogSize()))
                .build();

        appender = chronicle.acquireAppender();
        queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
        writerThread.start();
    }

    @VisibleForTesting
    public ChronicleWriter(ChronicleQueue chronicle) {
        this.chronicle = chronicle;
        appender = chronicle.acquireAppender();
        queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
        writerThread.start();
    }

    public void put(WriteMarshallable marshallable) throws InterruptedException {
        if (!active) {
            throw new IllegalStateException("Chronicle audit writer has been deactivated");
        }

        queue.put(marshallable);
    }

    private void writerLoop() {
        try {
            while (active) {
                WriteMarshallable marshallable = queue.take();
                appender.writeDocument(marshallable);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public synchronized void close() {
        if (!active) {
            return;
        }

        active = false;
        try {
            writerThread.interrupt();
            writerThread.join(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        chronicle.close();
    }
}
