package me.jeff.ignitepoc.chronicle.consumers;

import me.jeff.ignitepoc.chronicle.records.MRecord;
import me.jeff.ignitepoc.chronicle.serialization.JsonSerializer;
import me.jeff.ignitepoc.chronicle.values.UntypedValues;
import org.junit.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static me.jeff.ignitepoc.chronicle.consumers.ChronicleConsumer.CHRONICLE_PATH_KEY;

public class ChronicleConsumerMT {

    public static final String TMP_CHRONICLE = "/tmp/chronicle";

    @Test
    public void testRealWorldData() {
        Properties properties = new Properties();
        properties.setProperty(CHRONICLE_PATH_KEY, TMP_CHRONICLE);
        ChronicleProducer<UntypedValues> producer = new ChronicleProducer<>(properties, new JsonSerializer<>());

        Thread thread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                producer.send(
                        UntypedValues.builder()
                                .source("jeff")
                                .prefix("")
                                .timestamp(Instant.now().toEpochMilli())
                                .values(Collections.singletonMap("a", "b"))
                                .build()
                );
                try {
                    TimeUnit.MILLISECONDS.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        ChronicleSource chronicleSource = new ChronicleSource(TMP_CHRONICLE, "consumer", getManager());

        chronicleSource.init();

        while (chronicleSource.hasRemaining()) {
            MRecord record = chronicleSource.get();

            System.out.println(record);
        }

        chronicleSource.close();
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
