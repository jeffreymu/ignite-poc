package me.jeff.ignitepoc.chronicle.cdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtils.class);

    /**
     * Display the stacktrace contained in an exception.
     *
     * @param exception Exception
     * @return String with the output from printStackTrace
     * @see Exception.printStackTrace()
     **/
    public static String getExceptionStackTrace(Exception exception) {
        final StringBuilder sb = new StringBuilder(1024);
        sb.append(exception.getMessage());
        sb.append("\n");
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            exception.printStackTrace(pw);
            sb.append(sw.toString());
        } catch (Exception e) {
            LOGGER.error("Exception while converting exception's stack trace to string!\n{}",
                    e.getMessage());
        }
        return sb.toString();
    }

}
