package me.jeff.ignitepoc.chronicle.order;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.wire.ReadMarshallable;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.WriteMarshallable;

public class CrossingOrderHolder implements ReadMarshallable, WriteMarshallable {

    private int orderId;
    private byte[] content;

    public CrossingOrderHolder() {
    }

    public CrossingOrderHolder(int orderId, byte[] content) {
        this.orderId = orderId;
        this.content = content;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public void writeMarshallable(WireOut wire) {
        wire.bytes().writeInt(orderId);
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
        orderId = raw.readInt();
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
        // TODO Auto-generated method stub
        return ReadMarshallable.super.usesSelfDescribingMessage();
    }
}
