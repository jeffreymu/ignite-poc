package me.jeff.ignitepoc.chronicle;

import me.jeff.ignitepoc.chronicle.order.CrossingOrderHolder;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrossingOrderHolderTest {

    private final static List<CrossingOrderHolder> order1 = new ArrayList<>(Arrays.asList(
            new CrossingOrderHolder(11, "Lob011".getBytes())));

    private void processQueueBeforeRestart() throws IOException {
        final String tmpDir = System.getProperty("java.io.tmpdir");
        final Path queuesRoot = FileSystems.getDefault().getPath(tmpDir);
        final String xid = "0000270016000000";

    }

    @Test
    public void test() throws IOException {
        CrossingOrderHolderTest test = new CrossingOrderHolderTest();

    }
}
