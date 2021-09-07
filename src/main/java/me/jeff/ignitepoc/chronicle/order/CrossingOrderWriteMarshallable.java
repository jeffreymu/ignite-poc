package me.jeff.ignitepoc.chronicle.order;

import me.jeff.ignitepoc.chronicle.common.FieldSelector;
import net.openhft.chronicle.wire.ValueOut;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.WriteMarshallable;
import org.jetbrains.annotations.NotNull;

public class CrossingOrderWriteMarshallable implements WriteMarshallable {

    private final CrossingOrder crossingOrder;
    private final FieldSelector actualFields;

    public CrossingOrderWriteMarshallable(CrossingOrder crossingOrder, FieldSelector actualFields) {
        this.crossingOrder = crossingOrder;
        this.actualFields = actualFields;
    }

    @Override
    public void writeMarshallable(@NotNull WireOut wire) {
        wire.write("PortfolioID").int64(crossingOrder.getPortfolioID());
        wire.write("StrategyID").int64(crossingOrder.getStrategyID());
        wire.write("OrderID").bytes(crossingOrder.getOrderID().getBytes());
        wire.write("AccountNo").bytes(crossingOrder.getAccountNo().getBytes());
        wire.write("SubOrders").object(crossingOrder.getSubOrders());

    }

    @Override
    public void writeValue(@NotNull ValueOut out) {

    }

    @Override
    public boolean usesSelfDescribingMessage() {
        return false;
    }
}
