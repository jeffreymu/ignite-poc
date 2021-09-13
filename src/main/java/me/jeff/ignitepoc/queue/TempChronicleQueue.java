package me.jeff.ignitepoc.queue;

import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class TempChronicleQueue implements Closeable {

    private final ExcerptTailer tailer;
    private final ExcerptAppender appender;
    private final ChronicleQueue queue;
    private final Path tempDirectory;

    public TempChronicleQueue() throws IOException {
        tempDirectory = Files.createTempDirectory(getClass().getName());
        queue = ChronicleQueue.single(tempDirectory.toFile().getAbsolutePath());
        appender = queue.acquireAppender();
        tailer = queue.createTailer();

    }

    public ChronicleQueue getQueue() {
        return queue;
    }

    public ExcerptTailer createTailer() {
        return tailer;
    }

    public ExcerptAppender acquireAppender() {
        return appender;
    }

    @Override
    public void close() throws IOException {
        queue.close();
        int successMaxTimes = 5;

        while (successMaxTimes > 0) {
            try {
                System.gc();
                Thread.sleep(900);
                FileUtils.deleteDirectory(tempDirectory.toFile());
                return;
            } catch (IOException | InterruptedException e) {
                successMaxTimes--;
            }
        }
        log.info("Cannot delete old queue files");

    }

}
