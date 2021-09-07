package me.jeff.ignitepoc.chronicle.order;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Order {

    protected long portfolioID;
    protected String accountNo;
    protected long strategyID;
    protected String combiNo;
    protected String orderID;
    protected String parentOrderID;

}
