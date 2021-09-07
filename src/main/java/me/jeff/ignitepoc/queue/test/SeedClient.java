package me.jeff.ignitepoc.queue.test;

import me.jeff.ignitepoc.queue.api.service.CommandHandler;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import static java.lang.String.format;
import static me.jeff.ignitepoc.queue.ConcertFactory.createTickets;

public class SeedClient {

    public static void main(String[] args) throws Exception {
//        String createConcertQueuePath = format("%s/%s", OS.getTarget(), "createConcertQueue");
//        System.out.println("Queue Path : " + createConcertQueuePath);
//        try (ChronicleQueue queue = SingleChronicleQueueBuilder.binary(createConcertQueuePath).build()) {
//            CommandHandler commandHandler = queue.acquireAppender()
//                    .methodWriterBuilder(CommandHandler.class)
//                    .get();
//            createConcerts().stream().forEachOrdered(commandHandler::onCreateConcert);
//        }

        String createHandlerQueuePath = format("%s/%s", OS.getTarget(), "commandHandlerQueue");
        System.out.println("Queue Path : " + createHandlerQueuePath);
        try (ChronicleQueue queue = SingleChronicleQueueBuilder.binary(createHandlerQueuePath).build()) {
            CommandHandler commandHandler = queue.acquireAppender()
                    .methodWriterBuilder(CommandHandler.class)
                    .get();
            createTickets().stream().forEachOrdered(commandHandler::onTicketPurchase);
        }
    }

}
