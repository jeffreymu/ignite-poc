package me.jeff.ignitepoc.queue.api.service;


import me.jeff.ignitepoc.queue.command.CreateConcert;
import me.jeff.ignitepoc.queue.command.TicketPurchase;

public interface CommandHandler {
    void onCreateConcert(CreateConcert createConcert);

    void onTicketPurchase(TicketPurchase ticketPurchase);
}

