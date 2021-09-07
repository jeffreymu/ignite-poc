package me.jeff.ignitepoc.queue;

import me.jeff.ignitepoc.grpc.PersonEntity;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.io.IOException;

public class ChronicleQueueOutMain {

    public static void main(String[] args) throws IOException {
        String path = "queue-poc-daily";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
        ExcerptTailer tailer = queue.createTailer();
        byte[] data = null; //readByte(tailer);
        PersonEntity.Person p = PersonEntity.Person.parseFrom(data);
        System.out.println(p.getName());
    }

}
