package me.jeff.ignitepoc.chronicle.order;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CrossingOrder {

    private String orderID;
    private long portfolioID;
    private String accountNo;
    private long strategyID;
    private List<Order> subOrders;


}
