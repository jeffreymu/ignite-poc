package me.jeff.ignitepoc.queue;

import me.jeff.ignitepoc.grpc.PersonEntity;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.io.IOException;
import java.util.Arrays;

public class ChronicleQueueInputMain {

    public static void main(String[] args) throws IOException {
        String path = "queue-poc-daily";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.DAILY).build();
        ExcerptAppender appender = queue.acquireAppender();

        for (int i=0; i<100_000; i++) {
            PersonEntity.Person.Builder personBuilder = PersonEntity.Person.newBuilder();
            personBuilder.setId(i);
            personBuilder.setName("jeff");
            personBuilder.setEmail("123456@qq.com");

            PersonEntity.Person personEntity = personBuilder.build();
            System.out.println("protobuf bytes[]:" + Arrays.toString(personEntity.toByteArray()));
            System.out.println("protobuf size: " + personEntity.toByteString().size());
            byte[] bytes = personEntity.toByteArray();
            appender.writeBytes(b -> b.write(bytes));
//        writeByte(appender, bytes);
        }
//        PersonEntity.Person p = PersonEntity.Person.parseFrom(bytes);
//        System.out.println(p.getName());
    }


}
