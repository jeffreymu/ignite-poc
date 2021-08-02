package me.jeff.ignitepoc.cache;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StockCache extends MoICache {

    private String stockCode;
    private String stockName;
    private Double lastPrice;
}
