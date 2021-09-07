package me.jeff.ignitepoc.chronicle.order;

import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.wire.ReadMarshallable;
import net.openhft.chronicle.wire.ValueIn;
import net.openhft.chronicle.wire.WireIn;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class CrossingOrderReadMarshallable implements ReadMarshallable {

    private CrossingOrder crossingOrder;

    public CrossingOrderReadMarshallable() {
    }

    @Override
    public void readMarshallable(@NotNull WireIn wire) throws IORuntimeException {
        if (crossingOrder != null) {
            throw new IORuntimeException("Tried to read from wire with used marshallable");
        }

        crossingOrder = readBitmappedRecord(wire);
        log.info(crossingOrder.toString());
    }

    private CrossingOrder readBitmappedRecord(WireIn wire) {
        CrossingOrder crossingOrder = CrossingOrder.builder().build();
        crossingOrder.setPortfolioID(wire.read("PortfolioID").int64());
        crossingOrder.setStrategyID(wire.read("StrategyID").int64());
        crossingOrder.setAccountNo(new String(wire.read("AccountNo").bytes()));
        crossingOrder.setOrderID(new String(wire.read("OrderID").bytes()));
        crossingOrder.setSubOrders(wire.read("SubOrders").list(Order.class));
        return crossingOrder;
    }

    @Override
    public void unexpectedField(Object event, ValueIn valueIn) {

    }

    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }

    public CrossingOrder getCrossingOrder() {
        if (crossingOrder == null) {
            throw new IllegalStateException("No record has been read from the wire");
        }

        return crossingOrder;
    }
}
