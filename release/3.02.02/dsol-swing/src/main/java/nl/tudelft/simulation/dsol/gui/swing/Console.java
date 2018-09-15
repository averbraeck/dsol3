package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.Color;
import java.util.EnumSet;
import java.util.Set;

import javax.swing.JTextArea;

import org.pmw.tinylog.Configuration;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.LogEntry;
import org.pmw.tinylog.writers.LogEntryValue;
import org.pmw.tinylog.writers.Writer;

/**
 * <br>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at providing policy
 * analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL simulation engine and
 * the agent-based modeling formalism. See for project information <a href="https://simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>. The project is a co-operation between TU Delft, Systems Engineering and Simulation
 * Department (Netherlands) and NUDT, Simulation Engineering Department (China). This software is licensed under the BSD
 * license. See license.txt in the main project.
 * @version May 4, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class Console extends JTextArea
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    protected LogWriter logWriter;

    /**
     * Constructor for Console.
     */
    public Console()
    {
        super();
        this.setEditable(false);
        this.setForeground(Color.RED);
        this.logWriter = new LogWriter(this);
        Configurator.currentConfig().writer(this.logWriter).activate();
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
        /** */
        private JTextArea textArea;

        /**
         * @param textArea the text area to write the messages to.
         */
        public LogWriter(final JTextArea textArea)
        {
            this.textArea = textArea;
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
        public void write(LogEntry logEntry) throws Exception
        {
            if (logEntry.getException() != null)
            {
                this.textArea.append("-" + logEntry.getLevel() + "  " + logEntry.getException() + "  "
                        + logEntry.getMessage() + " \n");
            }
            else
            {
                this.textArea.append("-" + logEntry.getLevel() + "  " + logEntry.getMessage() + " \n");
            }
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
