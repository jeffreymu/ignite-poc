package me.jeff.ignitepoc.chronicle.consumers;

import java.io.Serializable;

public interface ConsumerManager extends AutoCloseable, Serializable {

    long getOffset(String consumer);

    void acknowledgeOffset(String consumer, long offset, boolean useAcknowledgeRate);

}
