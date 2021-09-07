package me.jeff.ignitepoc.queue.translator;

import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.util.Scanner;

public class TextInputMain {

    public static void main(String[] args) {
        String path_en = "text-queue-en";
        SingleChronicleQueue queue_en = SingleChronicleQueueBuilder.binary(path_en).build();
        MessageConsumer messageConsumer = queue_en.acquireAppender().methodWriter(MessageConsumer.class);
        Scanner read = new Scanner(System.in);
        while (true) {
            System.out.println("type something");
            String line = read.nextLine();
            if (line.isEmpty())
                break;
            messageConsumer.onMessage(line);
        }
        System.out.println("... bye.");
    }

}
