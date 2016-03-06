package org.melchor629.engine.utils.logger;

import java.time.LocalDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Formatter for file output
 */
public class FileFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        switch(record.getLevel().intValue()) {
            case 1000:
                sb.append("E");
                break;
            case 900:
                sb.append("W");
                break;
            case 800:
                sb.append("I");
                break;
            case 700:
                sb.append("D");
                break;
            default:
                sb.append("T");
        }

        sb.append(LocalDateTime.now().toString());
        sb.append(" [");
        if(Logger.threadEnabled()) {
            sb.append(Thread.currentThread().getName()).append(" - ");
        }
        sb.append(record.getLoggerName()).append("] ");
        sb.append(record.getMessage());
        sb.append("\n");

        return sb.toString();
    }
}
