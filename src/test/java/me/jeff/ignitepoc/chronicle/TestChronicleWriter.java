package me.jeff.ignitepoc.chronicle;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.wire.WriteMarshallable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class TestChronicleWriter {
    @Mock
    private ChronicleQueue mockChronicleQueue;

    @Mock
    private ExcerptAppender mockAppender;

    @Mock
    private WriteMarshallable marshallable;

    private ChronicleWriter writer;

    @Before
    public void before() {
        when(mockChronicleQueue.acquireAppender()).thenReturn(mockAppender);
        writer = new ChronicleWriter(mockChronicleQueue);
    }

    @After
    public void after() {
        verifyNoMoreInteractions(mockChronicleQueue);
        verifyNoMoreInteractions(mockAppender);
    }

    @Test
    public void putOneAndClose() throws Exception {
        writer.put(marshallable);

        Thread.sleep(50);
        writer.close();

        verify(mockAppender).writeDocument(eq(marshallable));
        verify(mockChronicleQueue).close();
    }

    @Test
    public void closeAndPutOne() throws Exception
    {
        new Thread(writer::close).start();

        Thread.sleep(100);
        assertThatIllegalStateException()
                .isThrownBy(() -> writer.put(marshallable));

        verify(mockChronicleQueue).close();
    }

    @Test
    public void putOneAndInterruptOnClose() throws Exception
    {
        Thread testThread = Thread.currentThread();
        doAnswer(invocation -> {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException ignored)
            {
                testThread.interrupt();
                Thread.sleep(100);
                Thread.currentThread().interrupt();
            }
            return null;
        }).when(mockAppender).writeDocument(any(WriteMarshallable.class));

        writer.put(marshallable);
        Thread.sleep(50);

        writer.close();

        verify(mockAppender).writeDocument(eq(marshallable));
        verify(mockChronicleQueue).close();
        assertThat(Thread.currentThread().isInterrupted()).isTrue();

        // Clear interrupt flag
        Thread.interrupted();
    }

    @Test
    public void closeQueueOnceOnly()
    {
        writer.close();
        writer.close();

        verify(mockChronicleQueue, times(1)).close();
    }
}
