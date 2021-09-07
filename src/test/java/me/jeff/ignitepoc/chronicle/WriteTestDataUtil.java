package me.jeff.ignitepoc.chronicle;

import me.jeff.ignitepoc.chronicle.common.AuditRecordWriteMarshallable;
import me.jeff.ignitepoc.chronicle.common.FieldSelector;
import me.jeff.ignitepoc.chronicle.common.record.AuditOperation;
import me.jeff.ignitepoc.chronicle.common.record.AuditRecord;
import me.jeff.ignitepoc.chronicle.common.record.Status;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WriteTestDataUtil {
    public static void main(String[] args) throws Exception {
        String version = "2"; // Set the version here!

        // Data
        AuditRecord record = SimpleAuditRecord.builder()
                .withClientAddress(new InetSocketAddress(InetAddress.getByName("0.1.2.3"), 777))
                .withCoordinatorAddress(InetAddress.getByName("4.5.6.7"))
                .withUser("bob")
                .withBatchId(UUID.fromString("bd92aeb1-3373-4d6a-b65a-0d60295f66c9"))
                .withStatus(Status.SUCCEEDED)
                .withOperation(mockOperation("SELECT SOMETHING", "SELECT SOMETHING NAKED"))
                .withTimestamp(1554188832013L)
                .withSubject("bob-the-subject")
                .build();

        // Write Data to Queue
        ChronicleQueue chronicleQueue = SingleChronicleQueueBuilder
                .single(new File("common/src/test/resources/q" + version))
                .blockSize(1024)
                .build();
        ExcerptAppender appender = chronicleQueue.acquireAppender();

        appender.writeDocument(new AuditRecordWriteMarshallable(record, FieldSelector.DEFAULT_FIELDS));
        appender.writeDocument(new AuditRecordWriteMarshallable(record, FieldSelector.NO_FIELDS));
        appender.writeDocument(new AuditRecordWriteMarshallable(record, FieldSelector.ALL_FIELDS));
        appender.writeDocument(new AuditRecordWriteMarshallable(record, FieldSelector.fromFields(asList("USER", "OPERATION_NAKED", "STATUS", "SUBJECT")))); // Custom fields

        chronicleQueue.close();
    }

    private static AuditOperation mockOperation(String operation, String nakedOperation) {
        AuditOperation operationMock = mock(AuditOperation.class);
        when(operationMock.getOperationString()).thenReturn(operation);
        when(operationMock.getNakedOperationString()).thenReturn(nakedOperation);
        return operationMock;
    }
}
