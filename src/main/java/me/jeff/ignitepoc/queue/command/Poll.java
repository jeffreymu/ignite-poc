package me.jeff.ignitepoc.queue.command;

import net.openhft.chronicle.wire.AbstractMarshallable;

public class Poll extends AbstractMarshallable {
    public final long accountId;
    public final long version;

    public Poll(long accountId, long version) {
        this.accountId = accountId;
        this.version = version;
    }
}
