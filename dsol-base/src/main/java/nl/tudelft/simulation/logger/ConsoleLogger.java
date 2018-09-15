package nl.tudelft.simulation.logger;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.Writer;

import nl.tudelft.simulation.language.Throw;

/**
 * Easy way to set the LogLevel for the entire simulation. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class ConsoleLogger
{
    /** the console writer, replacing the default one. */
    protected static Writer consoleWriter;

    /** default message format. */
    private static final String defaultMessageFormat = "{class_name}.{method}:{line} {message|indent=4}";

    /** current message format. */
    private static String messageFormat = defaultMessageFormat;

    /** the current logging level. */
    private static Level level = Level.INFO;

    /**
     * Create a new logger for the system console. Note that this REPLACES current loggers, so create this class before
     * e.g. swing console loggers are created.
     */
    public static void create()
    {
        Throw.when(consoleWriter != null, RuntimeException.class, "ConsoleLogger has already been initialized");
        consoleWriter = new ConsoleWriter();
        Configurator.currentConfig().writer(consoleWriter, level, messageFormat).activate();
    }

    /**
     * Set a new logging level for the default logger(s).
     * @param newLevel the new logging level to use
     */
    public static void setLevel(final Level newLevel)
    {
        Throw.whenNull(consoleWriter, "ConsoleLogger has not been initialized");
        Configurator.currentConfig().removeWriter(consoleWriter).activate();
        level = newLevel;
        Configurator.currentConfig().addWriter(consoleWriter, level, messageFormat).activate();
    }

    /**
     * Set a new logging format for the system console logger. A few popular placeholders that can be used:<br>
     * - {class} Fully-qualified class name where the logging request is issued<br>
     * - {class_name} Class name (without package) where the logging request is issued<br>
     * - {date} Date and time of the logging request, e.g. {date:yyyy-MM-dd HH:mm:ss} [SimpleDateFormat]<br>
     * - {level} Logging level of the created log entry<br>
     * - {line} Line number from where the logging request is issued<br>
     * - {message} Associated message of the created log entry<br>
     * - {method} Method name from where the logging request is issued<br>
     * - {package} Package where the logging request is issued<br>
     * @see <a href="https://tinylog.org/configuration#format">https://tinylog.org/configuration</a>
     * @param newMessageFormat the new formatting pattern to use
     */
    public static void setMessageFormat(final String newMessageFormat)
    {
        Throw.whenNull(consoleWriter, "ConsoleLogger has not been initialized");
        Configurator.currentConfig().removeWriter(consoleWriter).activate();
        messageFormat = newMessageFormat;
        Configurator.currentConfig().addWriter(consoleWriter, level, newMessageFormat).activate();
    }
}
