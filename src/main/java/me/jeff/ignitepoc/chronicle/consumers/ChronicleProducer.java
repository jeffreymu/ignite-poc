package me.jeff.ignitepoc.chronicle.consumers;

import com.google.common.base.Preconditions;
import me.jeff.ignitepoc.chronicle.serialization.Serializer;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static me.jeff.ignitepoc.chronicle.consumers.ChronicleConsumer.CHRONICLE_PATH_KEY;

public class ChronicleProducer<T> implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(ChronicleProducer.class);

    private final ChronicleQueue chronicleQueue;

    private final Serializer<T> serializer;
    private final ExcerptAppender appender;

    /**
     * Creates a Chronicle Consumer with the given Properties.
     *
     * @param properties
     */
    public ChronicleProducer(Properties properties, Serializer<T> serializer) {
        Preconditions.checkArgument(properties.containsKey(CHRONICLE_PATH_KEY),
                "No chronicle path given.");
        Preconditions.checkNotNull(serializer);

        this.serializer = serializer;

        String path = properties.getProperty(CHRONICLE_PATH_KEY);

        logger.info("Starting Chronicle Producer for folder {}", path);

        chronicleQueue = SingleChronicleQueueBuilder
                .single()
                .path(path)
                .build();

        appender = chronicleQueue.acquireAppender();
    }

    public boolean send(T value) {
        byte[] bytes = serializer.serialize(value);
        try (final DocumentContext dc = appender.writingDocument()) {
            dc.wire().write(() -> "msg").bytes(bytes);
            if (logger.isTraceEnabled()) {
                logger.trace("Data {} was store to index {}", new String(bytes), dc.index());
            }
            return true;
        } catch (Exception e) {
            logger.warn("Unable to store value " + new String(bytes) + " to chronicle", e);
            return false;
        }
    }

    @Override
    public void close() {
        logger.info("Closing Chronicle Producer");
        chronicleQueue.close();
    }
}
