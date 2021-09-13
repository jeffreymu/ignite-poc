package me.jeff.ignitepoc.chronicle.consumers;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChronicleSourceTest {

    @Test
    public void initialize() {
        ChronicleConsumer consumer = Mockito.mock(ChronicleConsumer.class);
        ChronicleSource source = new ChronicleSource(() -> consumer);

        source.init();

        assertTrue(source.hasRemaining());

        source.get();
        source.close();

        // Assert poll on source
        verify(consumer, times(1)).poll();
    }

}
