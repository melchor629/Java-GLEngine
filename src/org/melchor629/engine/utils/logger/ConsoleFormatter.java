package org.melchor629.engine.utils.logger;

import java.time.LocalDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Formatter for output text to ANSI terminal
 */
public class ConsoleFormatter extends Formatter {
    private static final int RED = 31;
    private static final int GREEN = 32;
    private static final int YELLOW = 33;
    private static final int BLUE = 34;
    private static final int WHITE = 37;

    private static final String RESET_COLORS = "\033[39;49m";

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        if(Logger.colorsEnabled()) {
            switch(record.getLevel().intValue()) {
                case 1000:
                    sb.append(ansiEscapeSequence(RED));
                    break;
                case 900:
                    sb.append(ansiEscapeSequence(YELLOW));
                    break;
                case 800:
                    sb.append(ansiEscapeSequence(WHITE));
                    break;
                case 700:
                    sb.append(ansiEscapeSequence(GREEN));
                    break;
                default:
                    sb.append(ansiEscapeSequence(BLUE));
            }
        } else {
            switch(record.getLevel().intValue()) {
                case 1000:
                    sb.append("ERROR");
                    break;
                case 900:
                    sb.append("WARN ");
                    break;
                case 800:
                    sb.append("INFO ");
                    break;
                case 700:
                    sb.append("DEBUG");
                    break;
                default:
                    sb.append("TRACE");
            }
        }

        sb.append(LocalDateTime.now().toString());
        sb.append(" [");
        if(Logger.threadEnabled()) {
            sb.append(Thread.currentThread().getName()).append(" - ");
        }
        sb.append(record.getLoggerName()).append("] ");
        sb.append(record.getMessage());
        if(Logger.colorsEnabled()) {
            sb.append(RESET_COLORS);
        }
        sb.append("\n");

        return sb.toString();
    }

    private String ansiEscapeSequence(int... escapes) {
        StringBuilder ansiSeq = new StringBuilder("\033[");
        for(int escape : escapes) {
            ansiSeq.append(escape).append(";");
        }
        ansiSeq.setCharAt(ansiSeq.length() - 1, 'm');
        return ansiSeq.toString();
    }
}
