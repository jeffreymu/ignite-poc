package me.jeff.ignitepoc.queue;

import me.jeff.ignitepoc.grpc.PersonEntity;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ChronicleQueueOutMain {

    public static void main(String[] args) throws IOException {
        String path = "queue-poc";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
        ExcerptTailer tailer = queue.createTailer();
        byte[] data = readByte(tailer);
        PersonEntity.Person p = PersonEntity.Person.parseFrom(data);
        System.out.println(p.getName());
    }

    public static byte[] readByte(ExcerptTailer tailer) {
        byte[] data = null;
        Bytes<ByteBuffer> bytes = Bytes.elasticHeapByteBuffer(1024 * 128);
        boolean read = tailer.readBytes(bytes);
        if (read) {
            byte[] readData = bytes.underlyingObject().array();
            int len = (int) bytes.readRemaining();
            bytes.clear();
            data = Arrays.copyOf(readData, len);
        }
        return data;
    }
}
