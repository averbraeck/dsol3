/*
 * @(#) StyledTextFormatter.java Nov 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.logger.formatters;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A StyledTextFormatter <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:39:18 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a href="mailto:nlang@fbk.eur.nl">Niels Lang </a>
 */
public class StyledTextFormatter extends Formatter
{

    /** the DEFAULT_STYLE */
    public static final String STYLE_DEFAULT = "STYLE_DEFAULT";

    /** the SOURCE_STYLE */
    public static final String STYLE_SOURCE = "STYLE_SOURCE";

    /** the WARNING_STYLE */
    public static final String STYLE_WARNING = "STYLE_WARNING";

    /** THE FINE_STYLE */
    public static final String STYLE_FINE = "STYLE_FINE";

    /** THE ORIGIN_STYLE */
    public static final String STYLE_ORIGIN = "STYLE_ORIGIN";

    /** The separator used */
    public static final String SEPARATOR = "!!@@!!";

    /** show the origin */
    private boolean showOrigin = true;

    /** a date to use */
    private Date date = new Date();

    /** a dateFormatter to use */
    private DateFormat dateFormatter = DateFormat.getTimeInstance();

    /**
     * constructs a new StyledTextFormatter
     * @param showOrigin whether or not to show the origin
     */
    public StyledTextFormatter(final boolean showOrigin)
    {
        this.showOrigin = showOrigin;
    }

    /**
     * tags a message
     * @param tag the tag
     * @param message the message
     * @return String
     */
    private String tag(final String tag, final String message)
    {
        return "<" + tag + ">" + message + "</" + tag + ">" + StyledTextFormatter.SEPARATOR;
    }

    /**
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @Override
    public String format(final LogRecord record)
    {
        StringBuffer message = new StringBuffer();
        try
        {
            // Print level
            Level level = record.getLevel();
            this.date.setTime(record.getMillis());
            String levelLabel = this.dateFormatter.format(this.date) + " " + level.getName() + ": ";
            if (level.equals(Level.WARNING) || level.equals(Level.SEVERE))
            {
                message.append(this.tag(STYLE_WARNING, levelLabel));
            }
            else if (!level.equals(Level.INFO))
            {
                message.append(this.tag(STYLE_FINE, levelLabel));
            }
            String body = record.getMessage();
            if (body == null)
            {
                body = "null";
            }
            // Print source
            int sepIndex = body.indexOf(':');
            if (sepIndex != -1)
            {
                String source = body.substring(0, body.indexOf(':'));
                message.append(this.tag(STYLE_SOURCE, source + " "));
                body = body.substring(body.indexOf(':'));
            }
            // Print message
            message.append(this.tag(STYLE_DEFAULT, body + "\n"));
            if (this.showOrigin)
            {
                String sourceName = record.getLoggerName();
                if (record.getSourceClassName() != null)
                {
                    sourceName = record.getSourceClassName();
                }
                String methodName = "unknown";
                if (record.getSourceMethodName() != null)
                {
                    methodName = record.getSourceMethodName();
                }
                String originLog = "Origin: " + sourceName + "#" + methodName + "\n";
                message.append(this.tag(STYLE_ORIGIN, originLog));
                if (record.getThrown() != null)
                {
                    try
                    {
                        StringWriter stringWriter = new StringWriter();
                        PrintWriter printWriter = new PrintWriter(stringWriter);
                        record.getThrown().printStackTrace(printWriter);
                        printWriter.close();
                        message.append(this.tag(STYLE_ORIGIN, stringWriter.toString()));
                    }
                    catch (Exception exception)
                    {
                        // We neglegt this exception
                        exception = null;
                    }
                }
            }
            message.append(this.tag(STYLE_DEFAULT, "\n"));
        }
        catch (Throwable exception)
        {
            System.out.println("Logger formatter exception " + record);
        }
        return message.toString();
    }
}