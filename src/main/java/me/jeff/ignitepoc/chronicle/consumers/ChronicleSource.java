package me.jeff.ignitepoc.chronicle.consumers;

import me.jeff.ignitepoc.chronicle.records.MRecord;
import me.jeff.ignitepoc.chronicle.serialization.JsonDeserializer;
import me.jeff.ignitepoc.chronicle.values.UntypedValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class ChronicleSource implements MRecordSource {

    private static final Logger logger = LoggerFactory.getLogger(ChronicleSource.class);

    private final ChronicleConsumerFactory consumerFactory;
    private ChronicleConsumer<UntypedValues> consumer;

    /**
     * Creates a Source which instantiates a default consumer.
     *
     * @param path         path to the chronicle queue
     * @param consumerName Name for the consumer to use
     */
    public ChronicleSource(String path, String consumerName) {
        consumerFactory = new ChronicleConsumerFactoryImpl(path, consumerName);
    }

    public ChronicleSource(String path, String consumerName, ConsumerManager consumerManager) {
        consumerFactory = new ChronicleConsumerMemoryFactoryImpl(path, consumerName, consumerManager);
    }

    /**
     * Creates a Source which instantiates a default consumer with a custom acknowledgementRate
     *
     * @param path                  path to the chronicle queue
     * @param consumerName          Name for the consumer to use
     * @param acknowledgementRate   AcknowledgemntRate that indicates every xth offset will be commited
     */
    public ChronicleSource(String path, String consumerName,Long acknowledgementRate) {
        consumerFactory = new ChronicleConsumerFactoryImpl(path, consumerName,acknowledgementRate);
    }

    /**
     * For fine grained control or testing.
     *
     * @param consumerFactory Factory to build the consumer.
     */
    public ChronicleSource(ChronicleConsumerFactory consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    @Override
    public MRecord get() {
        return consumer.poll();
    }

    @Override
    public boolean hasRemaining() {
        return true;
    }

    @Override
    public void init() {
        // Instantiate the Consumer
        this.consumer = consumerFactory.create();
    }

    @Override
    public void close() {
        try {
            consumer.close();
        } catch (Exception e) {
            logger.debug("Exception during closing ChronicleConsumer", e);
        }
    }

    @Override
    public Kind getKind() {
        return Kind.INFINITE;
    }

    /**
     * Factory for testing.
     */
    @FunctionalInterface
    interface ChronicleConsumerFactory extends Serializable {

        ChronicleConsumer<UntypedValues> create();

    }

    /**
     * Default implementation of {@link ChronicleConsumerFactory}
     */
    private class ChronicleConsumerFactoryImpl implements ChronicleConsumerFactory {

        private final String path;
        private final String consumerName;
        private final Long acknowledgementRate;

        ChronicleConsumerFactoryImpl(String path, String consumerName) {
            this(path,consumerName,null);
        }

        ChronicleConsumerFactoryImpl(String path, String consumerName,Long acknowledgementRate) {
            this.path = path;
            this.consumerName = consumerName;
            this.acknowledgementRate = acknowledgementRate;
        }

        @Override
        public ChronicleConsumer<UntypedValues> create() {
            return ChronicleConsumer.<UntypedValues>builder()
                    .withPath(path)
                    .withConsumerName(consumerName)
                    .withAcknowledgementRate(acknowledgementRate)
                    .withDeserializer(new JsonDeserializer<>(UntypedValues.class))
                    .build();
        }
    }

    private class ChronicleConsumerMemoryFactoryImpl implements ChronicleConsumerFactory {

        private final String path;
        private final String consumerName;
        private final Long acknowledgementRate;
        private ConsumerManager consumerManager;

        ChronicleConsumerMemoryFactoryImpl(String path, String consumerName, ConsumerManager consumerManager) {
            this(path,consumerName,null, consumerManager);
        }

        ChronicleConsumerMemoryFactoryImpl(String path, String consumerName, Long acknowledgementRate , ConsumerManager consumerManager) {
            this.path = path;
            this.consumerName = consumerName;
            this.acknowledgementRate = acknowledgementRate;
            this.consumerManager = consumerManager;
        }

        @Override
        public ChronicleConsumer<UntypedValues> create() {
            return ChronicleConsumer.<UntypedValues>builder()
                    .withPath(path)
                    .withConsumerName(consumerName)
                    .withAcknowledgementRate(acknowledgementRate)
                    .withDeserializer(new JsonDeserializer<>(UntypedValues.class))
                    .withManager(consumerManager)
                    .build();
        }
    }
}
