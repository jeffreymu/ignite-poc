package me.jeff.ignitepoc.chronicle;

import me.jeff.ignitepoc.chronicle.audit.AuditEntry;
import me.jeff.ignitepoc.chronicle.audit.ChronicleAuditLogger;
import me.jeff.ignitepoc.chronicle.common.FieldSelector;
import me.jeff.ignitepoc.chronicle.common.record.SimpleAuditOperation;
import me.jeff.ignitepoc.chronicle.common.record.Status;
import net.openhft.chronicle.wire.ValueOut;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.WriteMarshallable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class TestChronicleAuditLogger {
    @Mock
    private ChronicleWriter mockWriter;

    @Mock
    private WireOut mockWire;

    @Mock
    private ValueOut mockValue;

    private ChronicleAuditLogger logger;


    @Before
    public void before() {
        logger = new ChronicleAuditLogger(mockWriter, FieldSelector.DEFAULT_FIELDS);
    }

    @After
    public void after() {
        verifyNoMoreInteractions(mockWire);
        verifyNoMoreInteractions(mockValue);
    }

    @Test
    public void singleStatement() throws Exception {
        AuditEntry expectedAuditEntry = likeGenericRecord().build();

        logger.log(expectedAuditEntry);

        assertThatWireMatchRecord(expectedAuditEntry);
    }


    private AuditEntry.Builder likeGenericRecord() throws UnknownHostException {
        return AuditEntry.newBuilder()
                .timestamp(Instant.parse("1993-07-27T18:15:30Z").toEpochMilli())
                .user("Javier Sotomayor")
                .client(new InetSocketAddress(InetAddress.getByName("2.45.2.45"), 245))
                .coordinator(InetAddress.getByName("5.6.7.8"))
                .operation(new SimpleAuditOperation("High Jump"))
                .status(Status.ATTEMPT);
    }

    private void assertThatWireMatchRecord(AuditEntry expectedAuditEntry) throws Exception {
        ArgumentCaptor<WriteMarshallable> marshallableArgumentCaptor = ArgumentCaptor.forClass(WriteMarshallable.class);

        verify(mockWriter).put(marshallableArgumentCaptor.capture());

        WriteMarshallable writeMarshallable = marshallableArgumentCaptor.getValue();
        when(mockWire.write(anyString())).thenReturn(mockValue);
        writeMarshallable.writeMarshallable(mockWire);

        verify(mockWire).write(eq("version"));
        verify(mockValue).int16(eq((short) 2));
        verify(mockWire).write(eq("type"));
        verify(mockValue).text(eq("ecaudit"));
        verify(mockWire).write(eq("fields"));

        if (expectedAuditEntry.getBatchId().isPresent()) {
            int bitmap = FieldSelector.DEFAULT_FIELDS.getBitmap();
            verify(mockValue).int32(eq(bitmap));
        } else {
            int bitmapWithoutBatch = FieldSelector.DEFAULT_FIELDS.withoutField(FieldSelector.Field.BATCH_ID).getBitmap();
            verify(mockValue).int32(eq(bitmapWithoutBatch));
        }

        verify(mockWire).write(eq("timestamp"));
        verify(mockValue).int64(eq(expectedAuditEntry.getTimestamp()));
        verify(mockWire).write(eq("client_ip"));
        verify(mockValue).bytes(eq(expectedAuditEntry.getClientAddress().getAddress().getAddress()));
        verify(mockWire).write(eq("client_port"));
        verify(mockValue).int32(eq(expectedAuditEntry.getClientAddress().getPort()));
        verify(mockWire).write(eq("coordinator_ip"));
        verify(mockValue).bytes(eq(expectedAuditEntry.getCoordinatorAddress().getAddress()));
        verify(mockWire).write(eq("user"));
        verify(mockValue).text(eq(expectedAuditEntry.getUser()));
        verify(mockWire).write(eq("status"));
        verify(mockValue).text(eq(expectedAuditEntry.getStatus().name()));
        verify(mockWire).write(eq("operation"));
        verify(mockValue).text(eq(expectedAuditEntry.getOperation().getOperationString()));

        if (expectedAuditEntry.getBatchId().isPresent()) {
            verify(mockWire).write(eq("batchId"));
            verify(mockValue).uuid(eq(expectedAuditEntry.getBatchId().get()));
        }
    }
}
