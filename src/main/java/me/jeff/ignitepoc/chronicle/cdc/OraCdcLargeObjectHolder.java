package me.jeff.ignitepoc.chronicle.cdc;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.wire.ReadMarshallable;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.WriteMarshallable;

public class OraCdcLargeObjectHolder implements ReadMarshallable, WriteMarshallable {

    /**
     * LOB object Id
     */
    private int lobId;
    /**
     * LOB content
     */
    private byte[] content;

    public OraCdcLargeObjectHolder() {
    }

    public OraCdcLargeObjectHolder(final int lobId, final byte[] content) {
        super();
        this.lobId = lobId;
        this.content = content;
    }

    public int getLobId() {
        return lobId;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public void writeMarshallable(WireOut wire) {
        wire.bytes().writeInt(lobId);
        if (content == null || content.length < 1) {
            wire.bytes().writeInt(-1);
        } else {
            wire.bytes().writeInt(content.length);
            wire.bytes().write(content);
        }
    }


    @Override
    public void readMarshallable(WireIn wire) throws IORuntimeException {
        Bytes<?> raw = wire.bytes();
        lobId = raw.readInt();
        final int length = raw.readInt();
        if (length > 0) {
            content = new byte[length];
            raw.read(content);
        } else {
            content = new byte[0];
        }
    }


    @Override
    public boolean usesSelfDescribingMessage() {
        return ReadMarshallable.super.usesSelfDescribingMessage();
    }
}
