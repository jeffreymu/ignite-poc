package me.jeff.ignitepoc.chronicle.consumers;

import me.jeff.ignitepoc.chronicle.records.MRecord;
import me.jeff.ignitepoc.chronicle.serialization.JsonDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChronicleConsumerTest {

    @Test
    public void buildWithBuilder_withManager() {
        JsonDeserializer<MRecord> deserializer = new JsonDeserializer<>(MRecord.class);
        ConsumerManager manager = getManager();
        ChronicleConsumer<MRecord> consumer = ChronicleConsumer.<MRecord>builder()
                .withConsumerName("name")
                .withPath("/tmp")
                .withDeserializer(deserializer)
                .withManager(manager)
                .build();

        // Assert it took the right deserializer
        assertEquals(deserializer, consumer.getDeserializer());
        // Assert it used right manager
        assertEquals(manager, consumer.getManager());
    }

    private ConsumerManager getManager() {
        return new ConsumerManager() {

            @Override
            public void close() {
            }

            @Override
            public long getOffset(String consumer) {
                return 0;
            }

            @Override
            public void acknowledgeOffset(String consumer, long offset, boolean useAcknowledgeRate) {
            }
        };
    }
}
