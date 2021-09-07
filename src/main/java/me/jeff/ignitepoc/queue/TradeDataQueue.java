package me.jeff.ignitepoc.queue;

import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Slf4j
@Component
public class TradeDataQueue {

    private final static String SINGLE_CHRONICLE_QUEUE_NAME = "queue-poc";
    private ExcerptAppender appender;
    private ExcerptTailer tailer;
    private SingleChronicleQueue queue;

    @PostConstruct
    public void init() {
        createSingleQueue(SINGLE_CHRONICLE_QUEUE_NAME);
    }

    public void createSingleQueue(String queueBinaryPath) {

        if(this.queue == null)
            this.queue = SingleChronicleQueueBuilder.binary(queueBinaryPath).build();

        if (this.queue != null) {
            this.appender = queue.acquireAppender();
            this.tailer = queue.createTailer();
        }
    }

    public void writeData(byte[] data) {
        Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer(1024 * 128);
        bytes.ensureCapacity(data.length);
        ByteBuffer byteBuffer = bytes.underlyingObject();
        byteBuffer.put(data);
        bytes.readPositionRemaining(0, byteBuffer.position());
        appender.writeBytes(bytes);
        byteBuffer.clear();
    }

    public byte[] readByte() {
        byte[] data = null;
        Bytes<ByteBuffer> bytes = Bytes.elasticHeapByteBuffer(1024 * 128);
        boolean read = this.tailer.readBytes(bytes);
        if (read) {
            byte[] readData = bytes.underlyingObject().array();
            int len = (int) bytes.readRemaining();
            bytes.clear();
            data = Arrays.copyOf(readData, len);
        }
        return data;
    }

}
