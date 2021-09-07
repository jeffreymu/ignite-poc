package me.jeff.ignitepoc.queue.api.service;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import me.jeff.ignitepoc.queue.api.domain.Seating;
import me.jeff.ignitepoc.queue.api.domain.Section;
import me.jeff.ignitepoc.queue.api.event.*;
import me.jeff.ignitepoc.queue.command.CreateConcert;
import me.jeff.ignitepoc.queue.command.TicketPurchase;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static me.jeff.ignitepoc.queue.api.event.AllocationRejected.RejectionReason.*;

public class ConcertService implements Concert.Observer, CommandHandler {

    private final EventHandler eventHandler;
    private final Long2ObjectMap<Concert> concertRepository = new Long2ObjectOpenHashMap<Concert>();
    private final AllocationApproved allocationApproved = new AllocationApproved();
    private final SectionUpdated sectionUpdated = new SectionUpdated();
    private long sectionVersion = 0;

    public ConcertService(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void onCreateConcert(CreateConcert createConcert) {
        HashMap<Section, Seating> seatingBySection = new HashMap<>();

        for (int i = 0, n = createConcert.numSections; i < n; i++) {
            SectionSeating sectionSeating = createConcert.sections.get(i);
            Section section = new Section(sectionSeating.sectionId,
                    sectionSeating.name,
                    sectionSeating.price);
            Seating seating = new Seating(sectionSeating.seats);

            seatingBySection.put(section, seating);
        }

        Concert concert = new Concert(createConcert.concertId,
                createConcert.name,
                createConcert.venue,
                seatingBySection);
        System.out.println("Concert ID : " + concert.getId() + ", name: " + concert.getName());
        concertRepository.put(concert.getId(), concert);
        concert.addObserver(this);
        ConcertCreated concertCreated = buildConcertCreated(createConcert);
        eventHandler.onConcertAvailable(concertCreated);
    }

    @Override
    public void onTicketPurchase(TicketPurchase ticketPurchase) {
        Concert concert = concertRepository.get(ticketPurchase.concertId);
        if (concert == null) {
            reject(ticketPurchase, CONCERT_DOES_NOT_EXIST);
            return;
        }
        System.out.println("Concert = " + concert.getName());
        Section section = concert.getSection(ticketPurchase.sectionId);
        if (section == null) {
            reject(ticketPurchase, SECTION_DOES_NOT_EXIST);
            return;
        }

        int numSeats = ticketPurchase.numSeats;
        if (concert.getSeating(section).getAvailableSeats() < numSeats) {
            reject(ticketPurchase, NOT_ENOUGH_SEATS);
            return;
        }

        concert.allocateSeating(section, numSeats);

        AllocationApproved allocationApproved = buildAllocationApproved(ticketPurchase);
        eventHandler.onAllocationApproved(allocationApproved);
    }

    private ConcertCreated buildConcertCreated(CreateConcert createConcert) {
        return new ConcertCreated(
                createConcert.concertId,
                createConcert.version,
                createConcert.name,
                createConcert.venue,
                createConcert.numSections,
                createConcert.sections);
    }

    private void reject(TicketPurchase ticketPurchase, AllocationRejected.RejectionReason reason) {
        AllocationRejected rejected = buildAllocationRejected(ticketPurchase, reason);
        eventHandler.onAllocationRejected(rejected);
    }

    @NotNull
    private AllocationApproved buildAllocationApproved(TicketPurchase ticketPurchase) {
        // assume the caller must copy anything it needs to retain.
        return allocationApproved.init(
                ticketPurchase.accountId,
                ticketPurchase.requestId,
                ticketPurchase.numSeats
        );
    }

    @NotNull
    private AllocationRejected buildAllocationRejected(TicketPurchase ticketPurchase, AllocationRejected.RejectionReason reason) {
        return new AllocationRejected(
                ticketPurchase.accountId,
                ticketPurchase.requestId,
                reason
        );
    }

    @Override
    public void onSeatsAllocated(Concert event, Section section, Seating seating) {
        sectionUpdated.init(
                event.getId(),
                section.getId(),
                sectionVersion,
                seating.getAvailableSeats()
        );
        sectionVersion++;
        // assume the caller must copy anything it needs to retain.
        eventHandler.onSectionUpdated(sectionUpdated);
    }

}
