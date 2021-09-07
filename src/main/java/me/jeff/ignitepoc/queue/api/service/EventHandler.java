package me.jeff.ignitepoc.queue.api.service;

import me.jeff.ignitepoc.queue.api.event.AllocationApproved;
import me.jeff.ignitepoc.queue.api.event.AllocationRejected;
import me.jeff.ignitepoc.queue.api.event.ConcertCreated;
import me.jeff.ignitepoc.queue.api.event.SectionUpdated;

public interface EventHandler {
    void onConcertAvailable(ConcertCreated concertCreated);

    void onAllocationApproved(AllocationApproved allocationApproved);

    void onAllocationRejected(AllocationRejected allocationRejected);

    void onSectionUpdated(SectionUpdated sectionUpdated);
}
