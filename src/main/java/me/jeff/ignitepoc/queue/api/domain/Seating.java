package me.jeff.ignitepoc.queue.api.domain;

public class Seating
{
    private int availableSeats;

    public Seating(int numAvailableSeats)
    {
        this.availableSeats = numAvailableSeats;
    }

    public int getAvailableSeats()
    {
        return availableSeats;
    }

    public void reserve(int seats)
    {
        availableSeats -= seats;
    }

}
