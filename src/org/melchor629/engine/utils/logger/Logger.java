package org.melchor629.engine.utils.logger;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

/**
 * Logger for everything in the engine. Is a wrapper for Java Logger
 * {@link java.util.logging.Logger} using a custom console output with colors by
 * log level and output to a file.
 * <p>
 * Terminal colors are disabled for Windows platform or in incompatible terminals.
 * To disable colors everywhere, set System Property
 * {@code org.melchor629.engine.disableColors} to true.
 * <p>
 * Output to a log file is disabled by default, but can be enabled with System
 * Property {@code org.melchor629.engine.loggerFile} to true.
 * <p>
 * By default, Logger will print the current thread name. To disable this option,
 * set System Property {@code org.melchor629.engine.disableShowThread} to true.
 * @see java.util.logging.Logger
 */
public class Logger {
    private static final String PROP = "org.melchor629.engine.";
    private static Map<String, Logger> loggers = new TreeMap<>();
    private java.util.logging.Logger javaLogger;

    static {
        java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");
        if(rootLogger.getHandlers()[0] instanceof ConsoleHandler) {
            ConsoleHandler c = (ConsoleHandler) rootLogger.getHandlers()[0];
            c.setFormatter(new ConsoleFormatter());
        }

        if(logFileEnabled()) {
            try {
                FileHandler f = new FileHandler("engine.log");
                f.setFormatter(new FileFormatter());
                rootLogger.addHandler(f);
            } catch(Exception e) {
                System.err.print("Cannot open \"engine.log\" to write logs in");
                RuntimeException r = new RuntimeException("Cannot open engine.log file");
                r.initCause(e);
                throw r;
            }
        }
    }

    static boolean colorsEnabled() {
        String systemProperty = System.getProperty(PROP+"disableColors", "false");
        String terminal = System.getenv("TERM");
        boolean b = !Boolean.parseBoolean(systemProperty);

        b = b && !System.getProperty("os.name").toLowerCase().contains("win");
        b = b && terminal != null && terminal.contains("xterm");

        return b;
    }

    static boolean threadEnabled() {
        String systemProperty = System.getProperty(PROP+"disableShowThread", "false");
        return !Boolean.parseBoolean(systemProperty);
    }

    static boolean logFileEnabled() {
        return Boolean.parseBoolean(System.getProperty(PROP+"loggerFile", "false"));
    }

    /**
     * Finds or creates a javaLogger for the given class with the default logging
     * level defined in LogManager from Java Logging.
     * @param clazz Class form which identify the javaLogger
     * @param <T> Type of the class
     * @return the javaLogger for the class
     * @see java.util.logging.LogManager
     */
    public static <T> Logger getLogger(Class<T> clazz) {
        if(loggers.containsKey(clazz.getName()))
            return loggers.get(clazz.getName());
        else {
            Logger log = new Logger(clazz);
            loggers.put(clazz.getName(), log);
            return log;
        }
    }

    private <T> Logger(Class<T> clazz) {
        javaLogger = java.util.logging.Logger.getLogger(clazz.getName());
    }

    //Basic logging methods

    /**
     * Logs a message with ERROR level.<br>
     * Only will log if the level of the javaLogger is ERROR or less.
     * @param msg message to log
     */
    public void error(String msg) {
        javaLogger.severe(msg);
    }

    /**
     * Logs a message with WARN level.<br>
     * Only will log if the level of the javaLogger is WARN or less
     * @param msg message to log
     */
    public void warn(String msg) {
        javaLogger.warning(msg);
    }

    /**
     * Logs a message with INFO level.<br>
     * Only will log if the level of the javaLogger is INFO or less
     * @param msg message to log
     */
    public void info(String msg) {
        javaLogger.info(msg);
    }

    /**
     * Logs a message with DEBUG level.<br>
     * Only will log if the level of the javaLogger is DEBUG or less
     * @param msg message to log
     */
    public void debug(String msg) {
        javaLogger.config(msg);
    }

    /**
     * Logs a message with TRACE level.<br>
     * Only will log if the level of the javaLogger is TRACE or less
     * @param msg message to log
     */
    public void trace(String msg) {
        javaLogger.fine(msg);
    }

    //Format String logging methods

    /**
     * Logs a formatted message with ERROR level.<br>
     * Only will log if the level of the javaLogger is ERROR or less
     * @param fmt formatted message to log
     * @param args format arguments
     * @see java.util.Formatter
     */
    public void error(String fmt, Object... args) {
        javaLogger.severe(String.format(fmt, args));
    }

    /**
     * Logs a formatted message with WARN level.<br>
     * Only will log if the level of the javaLogger is WARN or less
     * @param fmt formatted message to log
     * @param args format arguments
     * @see java.util.Formatter
     */
    public void warn(String fmt, Object... args) {
        javaLogger.warning(String.format(fmt, args));
    }

    /**
     * Logs a formatted message with INFO level.<br>
     * Only will log if the level of the javaLogger is INFO or less
     * @param fmt formatted message to log
     * @param args format arguments
     * @see java.util.Formatter
     */
    public void info(String fmt, Object... args) {
        javaLogger.info(String.format(fmt, args));
    }

    /**
     * Logs a formatted message with DEBUG level.<br>
     * Only will log if the level of the javaLogger is DEBUG or less
     * @param fmt formatted message to log
     * @param args format arguments
     * @see java.util.Formatter
     */
    public void debug(String fmt, Object... args) {
        javaLogger.config(String.format(fmt, args));
    }

    /**
     * Logs a formatted message with TRACE level.<br>
     * Only will log if the level of the javaLogger is TRACE or less
     * @param fmt formatted message to log
     * @param args format arguments
     * @see java.util.Formatter
     */
    public void trace(String fmt, Object... args) {
        javaLogger.fine(String.format(fmt, args));
    }

    //With throwable logging methods

    private Supplier<String> throwableSupplier(String msg, Throwable t, Object ... args) {
        return () -> {
            StringBuilder sb = new StringBuilder(String.format(msg, args));
            sb.append(": ").append(t.getMessage()).append('\n');
            for(StackTraceElement ste : t.getStackTrace()) {
                sb.append('\t').append(ste.toString()).append('\n');
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        };
    }

    /**
     * Logs a message with the throwable with ERROR level.<br>
     * Only will log if the level of the javaLogger is ERROR or less
     * @param msg message to log
     * @param t throwable
     * @see java.util.Formatter
     */
    public void throwable(String msg, Throwable t) {
        javaLogger.severe(throwableSupplier(msg, t));
    }

    /**
     * Logs a message with the throwable with DEBUG level.<br>
     * Only will log if the level of the javaLogger is DEBUG or less
     * @param msg message to log
     * @param t throwable
     * @see java.util.Formatter
     */
    public void debugThrowable(String msg, Throwable t) {
        javaLogger.config(throwableSupplier(msg, t));
    }

    /**
     * Logs a formatted message and the throwable with ERROR level.<br>
     * Only will log if the level of the javaLogger is ERROR or less
     * @param fmt formatted message to log
     * @param t throwable
     * @param args format arguments
     * @see java.util.Formatter
     */
    public void throwable(String fmt, Throwable t, Object... args) {
        javaLogger.severe(throwableSupplier(fmt, t, args));
    }

    /**
     * Logs a formatted message and the throwable with DEBUG level.<br>
     * Only will log if the level of the javaLogger is DEBUG or less
     * @param fmt formatted message to log
     * @param t throwable
     * @param args format arguments
     * @see java.util.Formatter
     */
    public void debugThrowable(String fmt, Throwable t, Object... args) {
        javaLogger.config(throwableSupplier(fmt, t, args));
    }
}
