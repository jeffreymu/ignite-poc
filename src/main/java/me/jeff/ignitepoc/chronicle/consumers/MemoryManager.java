package me.jeff.ignitepoc.chronicle.consumers;

import java.util.HashMap;
import java.util.Map;

public class MemoryManager implements ConsumerManager {

    private Map<String, Long> offsetMap = new HashMap<>();

    @Override
    public long getOffset(String consumer) {
        if (offsetMap.containsKey(consumer)) {
            return offsetMap.get(consumer);
        } else {
            return -1;
        }
    }

    @Override
    public void acknowledgeOffset(String consumer, long offset, boolean useAcknowledgeRate) {
        offsetMap.put(consumer, offset);
    }

    @Override
    public void close() throws Exception {
        // do nothing here
    }
}
