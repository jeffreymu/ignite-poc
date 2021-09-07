/*
 * Copyright 2016-2020 chronicle.software
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.jeff.ignitepoc.queue.micros;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;

public class SidedMarketDataCombiner implements SidedMarketDataListener {
    final MarketDataListener mdListener;
    final Map<String, TopOfBookPrice> priceMap = new TreeMap<>();

    public SidedMarketDataCombiner(MarketDataListener mdListener) {
        this.mdListener = mdListener;
    }

    @Override
    public void onSidedPrice(@NotNull SidedPrice sidedPrice) {
        TopOfBookPrice price = priceMap.computeIfAbsent(sidedPrice.symbol, TopOfBookPrice::new);
        if (price.combine(sidedPrice))
            mdListener.onTopOfBookPrice(price);
    }
}
