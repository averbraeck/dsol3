package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.Color;
import java.util.EnumSet;
import java.util.Set;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.pmw.tinylog.Configuration;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.LogEntry;
import org.pmw.tinylog.writers.LogEntryValue;
import org.pmw.tinylog.writers.Writer;

/**
 * The Console for the swing application where the log messages are displayed. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class Console extends JTextPane
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    protected LogWriter logWriter;

    /** current message format. */
    private String messageFormat = defaultMessageFormat;

    /** default message format. */
    private static final String defaultMessageFormat = "{class_name}.{method}:{line} {message|indent=4}";

    /** the current logging level. */
    private Level level = Level.INFO;

    /**
     * Constructor for Console.
     */
    public Console()
    {
        super();
        setEditable(false);
        this.logWriter = new LogWriter(this);
        Configurator.currentConfig().addWriter(this.logWriter, this.level, this.messageFormat).activate();
    }

    /**
     * Set a new logging format for the message lines of the Console writer. The default message format is:<br>
     * {class_name}.{method}:{line} {message|indent=4}<br>
     * <br>
     * A few popular placeholders that can be used:<br>
     * - {class} Fully-qualified class name where the logging request is issued<br>
     * - {class_name} Class name (without package) where the logging request is issued<br>
     * - {date} Date and time of the logging request, e.g. {date:yyyy-MM-dd HH:mm:ss} [SimpleDateFormat]<br>
     * - {level} Logging level of the created log entry<br>
     * - {line} Line number from where the logging request is issued<br>
     * - {message} Associated message of the created log entry<br>
     * - {method} Method name from where the logging request is issued<br>
     * - {package} Package where the logging request is issued<br>
     * @see <a href="https://tinylog.org/configuration#format">https://tinylog.org/configuration</a>
     * @param messageFormat the new formatting pattern to use
     */
    public void setLogMessageFormat(final String messageFormat)
    {
        Configurator.currentConfig().removeWriter(this.logWriter).activate();
        this.messageFormat = messageFormat;
        Configurator.currentConfig().addWriter(this.logWriter, this.level, this.messageFormat).activate();
    }

    /**
     * @param level the new log level for the Console
     */
    public void setLogLevel(final Level level)
    {
        Configurator.currentConfig().removeWriter(this.logWriter).activate();
        this.level = level;
        Configurator.currentConfig().addWriter(this.logWriter, this.level, this.messageFormat).activate();
    }

    /**
     * LogWriter takes care of writing the log records to the console. <br>
     * <br>
     * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved. See for project information
     * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
     * binary code of this software is proprietary information of Delft University of Technology.
     * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
     */
    public static class LogWriter implements Writer
    {
        /** the text pane. */
        JTextPane textPane;

        /** the document to write to. */
        StyledDocument doc;

        /** the color style. */
        Style style;

        /**
         * @param textPane the text area to write the messages to.
         */
        public LogWriter(final JTextPane textPane)
        {
            this.textPane = textPane;
            this.doc = textPane.getStyledDocument();
            this.style = textPane.addStyle("colorStyle", null);
        }

        /** {@inheritDoc} */
        @Override
        public Set<LogEntryValue> getRequiredLogEntryValues()
        {
            return EnumSet.of(LogEntryValue.RENDERED_LOG_ENTRY); // Only the final rendered log entry is required
        }

        /** {@inheritDoc} */
        @Override
        public void init(Configuration configuration) throws Exception
        {
            // nothing to do
        }

        /** {@inheritDoc} */
        @Override
        public synchronized void write(LogEntry logEntry) throws Exception
        {
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    switch (logEntry.getLevel())
                    {
                        case TRACE:
                            StyleConstants.setForeground(LogWriter.this.style, Color.DARK_GRAY);
                            break;

                        case DEBUG:
                            StyleConstants.setForeground(LogWriter.this.style, Color.BLUE);
                            break;

                        case INFO:
                            StyleConstants.setForeground(LogWriter.this.style, Color.BLACK);
                            break;

                        case WARNING:
                            StyleConstants.setForeground(LogWriter.this.style, Color.MAGENTA);
                            break;

                        case ERROR:
                            StyleConstants.setForeground(LogWriter.this.style, Color.RED);
                            break;

                        default:
                            break;
                    }
                    try
                    {
                        LogWriter.this.doc.insertString(LogWriter.this.doc.getLength(), logEntry.getRenderedLogEntry(),
                                LogWriter.this.style);
                    }
                    catch (Exception exception)
                    {
                        System.err.println("was not able to insert text in the Console");
                    }
                    LogWriter.this.textPane.setCaretPosition(LogWriter.this.doc.getLength());
                }
            };
            SwingUtilities.invokeLater(runnable);
        }

        /** {@inheritDoc} */
        @Override
        public void flush() throws Exception
        {
            // nothing to do
        }

        /** {@inheritDoc} */
        @Override
        public void close() throws Exception
        {
            // nothing to do
        }

    }
}
