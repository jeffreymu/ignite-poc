package me.jeff.ignitepoc.queue;

import me.jeff.ignitepoc.grpc.PersonEntity;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ChronicleQueueInputMain {

    public static void main(String[] args) throws IOException {
        String path = "queue-poc";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
        ExcerptAppender appender = queue.acquireAppender();

        PersonEntity.Person.Builder personBuilder = PersonEntity.Person.newBuilder();
        personBuilder.setId(1);
        personBuilder.setName("jeff");
        personBuilder.setEmail("123456@qq.com");

        PersonEntity.Person personEntity = personBuilder.build();
        System.out.println("protobuf bytes[]:" + Arrays.toString(personEntity.toByteArray()));
        System.out.println("protobuf size: " + personEntity.toByteString().size());
        byte[] bytes = personEntity.toByteArray();
//        appender.writeBytes(b -> b.write(bytes));
        writeByte(appender, bytes);

        PersonEntity.Person p = PersonEntity.Person.parseFrom(bytes);
        System.out.println(p.getName());
    }

    public static void writeByte(ExcerptAppender appender, byte[] data) {
        Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer(1024 * 128);
        bytes.ensureCapacity(data.length);

        ByteBuffer byteBuffer = bytes.underlyingObject();
        byteBuffer.put(data);
        bytes.readPositionRemaining(0, byteBuffer.position());
        appender.writeBytes(bytes);
        byteBuffer.clear();
    }
}
