package me.jeff.ignitepoc.queue.test;

import me.jeff.ignitepoc.queue.api.event.SectionSeating;
import me.jeff.ignitepoc.queue.api.service.CommandHandler;
import me.jeff.ignitepoc.queue.api.service.ConcertService;
import me.jeff.ignitepoc.queue.api.service.EventHandler;
import me.jeff.ignitepoc.queue.command.CreateConcert;
import me.jeff.ignitepoc.queue.command.TicketPurchase;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static org.apache.ignite.internal.util.lang.GridFunc.asList;

public class SaferReplayTest {

    private static final long concertId = 1;
    private static final long sectionId = 1;
    private static final int numSeats = 1;
    private static final long accountId = 12;
    private static final long requestId = 76;

    public static void main(String[] args) throws Exception {
        String createConcertQueuePath = format("%s/%s", OS.getTarget(), "createConcertQueuePath" + System.nanoTime());
        String commandHandlerQueuePath = format("%s/%s", OS.getTarget(), "commandHandlerQueuePath-" + System.nanoTime());
        String eventHandlerQueuePath = format("%s/%s", OS.getTarget(), "eventHandlerQueuePath-" + System.nanoTime());

        int numberOfEventsPerQueue = 4;
        // numberOfReadsPerQueue must be greater than numberOfEventsPerQueue to ensure than unexpected replay does not happens
        int numberOfReadsPerQueue = numberOfEventsPerQueue + 2;

        try (ChronicleQueue createConcertQueue = SingleChronicleQueueBuilder.binary(createConcertQueuePath).sourceId(1).build()) {
            CommandHandler commandHandler = createConcertQueue
                    .acquireAppender()
                    .methodWriterBuilder(CommandHandler.class)
                    .recordHistory(true)
                    .get();

            long currentConcertId = concertId;
            for (int i = 0; i < numberOfEventsPerQueue; i++) {
                CreateConcert createConcert = new CreateConcert(
                        currentConcertId,
                        0,
                        "Red Hot Chili Peppers",
                        "Albert Hall",
                        (short) 1,
                        asList(new SectionSeating(sectionId, "Section A", 58.50F, Integer.MAX_VALUE))
                );
                commandHandler.onCreateConcert(createConcert);
                currentConcertId++;
            }
        }

        AtomicInteger onCreateConcertCallsCount = new AtomicInteger(0);
        AtomicInteger onTicketPurchaseCallsCount = new AtomicInteger(0);
        for (int i = 0; i < numberOfReadsPerQueue; i++) {

            try (ChronicleQueue eventHandlerQueue = SingleChronicleQueueBuilder.binary(eventHandlerQueuePath).sourceId(99).build();
                 ChronicleQueue createConcertQueue = SingleChronicleQueueBuilder.binary(createConcertQueuePath).sourceId(1).build();
                 ChronicleQueue commandHandlerQueue = SingleChronicleQueueBuilder.binary(commandHandlerQueuePath).sourceId(2).build()) {
                EventHandler eventHandler = eventHandlerQueue
                        .acquireAppender()
                        .methodWriterBuilder(EventHandler.class)
                        .recordHistory(true)
                        .get();

                CommandHandler commandHandler = new CommandHandler() {
                    private ConcertService concertService = new ConcertService(eventHandler);

                    @Override
                    public void onCreateConcert(CreateConcert createConcert) {
                        onCreateConcertCallsCount.getAndIncrement();
                        concertService.onCreateConcert(createConcert);
                    }

                    @Override
                    public void onTicketPurchase(TicketPurchase ticketPurchase) {
                        onTicketPurchaseCallsCount.getAndIncrement();
                        concertService.onTicketPurchase(ticketPurchase);
                    }
                };

                MethodReader createConcertReader = createConcertQueue
                        .createTailer()
                        .afterLastWritten(eventHandlerQueue)
                        .methodReader(commandHandler);

                MethodReader commandHandlerReader = commandHandlerQueue
                        .createTailer()
                        .afterLastWritten(eventHandlerQueue)
                        .methodReader(commandHandler);

                createConcertReader.readOne();
                commandHandlerReader.readOne();
            }
        }

    }
}
