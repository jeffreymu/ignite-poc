package me.jeff.ignitepoc.chronicle.consumers;

import me.jeff.ignitepoc.chronicle.serialization.JsonDeserializer;
import me.jeff.ignitepoc.chronicle.serialization.JsonSerializer;
import me.jeff.ignitepoc.chronicle.values.UntypedValues;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ChronicleConsumerProducerTest {

    private static final Logger logger = LoggerFactory.getLogger(ChronicleConsumerProducerTest.class);

    @Test
    public void produceAndConsume_waitFornewMessage_blockNotNPE() throws Exception {
        String basePath = System.getProperty("java.io.tmpdir");
        String path = Files.createTempDirectory(Paths.get(basePath), "chronicle-")
                .toAbsolutePath()
                .toString();
        logger.info("Using temp path '{}'", path);

        Properties properties = new Properties();
        properties.put(ChronicleConsumer.CHRONICLE_PATH_KEY, path);
        properties.put(ChronicleConsumer.CHRONICLE_CONSUMER_KEY, "jeff");

        // Create before, because moves to end
        Thread thread;
        try (ChronicleConsumer<UntypedValues> consumer = new ChronicleConsumer<>(properties, new MemoryManager(), new JsonDeserializer<>(UntypedValues.class))) {
            try (ChronicleProducer<UntypedValues> producer = new ChronicleProducer<>(properties, new JsonSerializer<>())) {

                // Write
                UntypedValues values = UntypedValues.builder()
                        .prefix("")
                        .source("test")
                        .timestamp(Instant.now().toEpochMilli())
                        .values(Collections.singletonMap("key", "Julian"))
                        .build();

                producer.send(values);
                System.out.println("Sending= " + values.toString());
                // Read
                UntypedValues pv = consumer.poll();
                System.out.println("v1=" + pv.toString());
                // Do a second read which "waits" until another value is written
                thread = new Thread(() -> {
                    // Fail the test after two seconds

                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    try (ChronicleProducer<UntypedValues> producer2 = new ChronicleProducer<>(properties, new JsonSerializer<>())) {
                        UntypedValues newv = UntypedValues.builder()
                                .prefix("")
                                .source("test")
                                .timestamp(Instant.now().toEpochMilli())
                                .values(Collections.singletonMap("key", "Jeffrey"))
                                .build();
                        producer2.send(newv);
                        System.out.println("Sending= " + newv.toString());
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(2_000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    fail("Did not read the record");
                });

                // Start background thread that writes after 2 seconds
                thread.start();
                UntypedValues poll = consumer.poll();
                //System.out.println("vc=" + poll.toString());
                thread.interrupt();
                // Assert not null
                assertNull(poll);
            }
        }
    }

}
